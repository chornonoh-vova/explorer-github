package com.hbvhuwe.explorergithub.di

import com.hbvhuwe.explorergithub.ui.FileActivity
import com.hbvhuwe.explorergithub.ui.LoginActivity
import com.hbvhuwe.explorergithub.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, DbModule::class])
interface NetComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(userViewModel: UserViewModel)
    fun inject(reposViewModel: ReposViewModel)
    fun inject(repositoryViewModel: RepositoryViewModel)
    fun inject(filesViewModel: FilesViewModel)
    fun inject(fileViewModel: FileViewModel)
    fun inject(issuesViewModel: IssuesViewModel)
}