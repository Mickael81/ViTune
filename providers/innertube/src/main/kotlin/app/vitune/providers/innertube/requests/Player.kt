package app.vitune.providers.innertube.requests

import app.vitune.providers.innertube.Innertube
import app.vitune.providers.innertube.models.Context
import app.vitune.providers.innertube.models.PlayerResponse
import app.vitune.providers.innertube.models.bodies.PlayerBody
import app.vitune.providers.utils.runCatchingCancellable
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.generateNonce
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

private suspend fun Innertube.tryContexts(
    body: PlayerBody,
    vararg contexts: Context
): PlayerResponse? {
    contexts.forEach { context ->
        if (!currentCoroutineContext().isActive) return null

        logger.info("Trying ${context.client.clientName} ${context.client.clientVersion} ${context.client.platform}")
        val cpn = generateNonce(16).decodeToString()
        runCatchingCancellable {
            client.post(if (context.client.music) PLAYER_MUSIC else PLAYER) {
                setBody(
                    body.copy(
                        context = context,
                        cpn = cpn,
                        playbackContext = PlayerBody.PlaybackContext(
                            contentPlaybackContext = PlayerBody.PlaybackContext.ContentPlaybackContext(
                                signatureTimestamp = getSignatureTimestamp(context)
                            )
                        )
                    )
                )

                context.apply()

                parameter("t", generateNonce(12))
                header("X-Goog-Api-Format-Version", "2")
                parameter("id", body.videoId)
            }.body<PlayerResponse>().also { logger.info("Got $it") }
        }
            ?.getOrNull()
            ?.takeIf { it.isValid }
            ?.let {
                return it.copy(
                    cpn = cpn,
                    context = context
                )
            }
    }

    return null
}

private val PlayerResponse.isValid
    get() = playabilityStatus?.status == "OK" &&
        streamingData?.adaptiveFormats?.any { it.url != null || it.signatureCipher != null } == true

suspend fun Innertube.player(body: PlayerBody): Result<PlayerResponse?>? = runCatchingCancellable {
    tryContexts(
        body = body,
        Context.DefaultIOS,
        Context.DefaultWeb,
        Context.DefaultTV,
        Context.DefaultAndroidMusic
    )
}
