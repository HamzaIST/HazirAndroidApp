package technology.innovate.haziremployee.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.ui.attendance.AttendanceRepository
import technology.innovate.haziremployee.ui.check_in.ActionRepository
import technology.innovate.haziremployee.ui.forgot_password.ForgotPasswordRepository
import technology.innovate.haziremployee.ui.home.HomeRepository
import technology.innovate.haziremployee.ui.login.LoginRepository
import technology.innovate.haziremployee.ui.notifications.NotificationsRepository
import technology.innovate.haziremployee.ui.profile.ProfileRepository
import technology.innovate.haziremployee.ui.requests.RequestsRepository
import technology.innovate.haziremployee.utility.NetworkHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesLoginRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): LoginRepository = LoginRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesAttendanceRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): AttendanceRepository = AttendanceRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesActionRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): ActionRepository = ActionRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesHomeRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): HomeRepository = HomeRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesNotificationRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): NotificationsRepository = NotificationsRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesProfileRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): ProfileRepository = ProfileRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesForgotPasswordRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): ForgotPasswordRepository = ForgotPasswordRepository(retrofit, networkHelper)

    @Provides
    @Singleton
    fun providesRequestsRepository(
        retrofit: EmployeeEndpoint, networkHelper: NetworkHelper
    ): RequestsRepository = RequestsRepository(retrofit, networkHelper)

}