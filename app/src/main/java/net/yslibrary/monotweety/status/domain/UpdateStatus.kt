package net.yslibrary.monotweety.status.domain

import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.StatusRepository
import rx.Completable
import javax.inject.Inject

@UserScope
class UpdateStatus @Inject constructor(private val statusRepository: StatusRepository) {

  fun execute(status: String, inReplyToStatusId: Long? = null): Completable {
    return statusRepository.updateStatus(status, inReplyToStatusId)
  }
}