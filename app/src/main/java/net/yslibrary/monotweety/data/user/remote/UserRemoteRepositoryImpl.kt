package net.yslibrary.monotweety.data.user.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.services.AccountService
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.user.User
import rx.Single
import javax.inject.Inject
import com.twitter.sdk.android.core.models.User as TwitterUser

@UserScope
class UserRemoteRepositoryImpl @Inject constructor(private val accountService: AccountService) : UserRemoteRepository {
  override fun get(): Single<User> {
    return Single.fromEmitter<TwitterUser>({ emitter ->
      val call = accountService.verifyCredentials(false, true)
      call.enqueue(object : Callback<TwitterUser>() {
        override fun failure(exception: TwitterException?) {
          emitter.onError(exception)
        }

        override fun success(result: Result<TwitterUser>) {
          emitter.onSuccess(result.data)
        }
      })
      emitter.setCancellation { call.cancel() }
    }).map { user ->
      User(
          id = user.id,
          name = user.name,
          screenName = user.screenName,
          profileImageUrl = user.profileImageUrl,
          _updatedAt = -1 // updated in UserRepository
      )
    }
  }
}