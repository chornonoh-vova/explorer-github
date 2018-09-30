package com.hbvhuwe.explorergithub.di

import com.hbvhuwe.explorergithub.ui.LoginActivity
import com.hbvhuwe.explorergithub.viewmodel.FilesViewModel
import com.hbvhuwe.explorergithub.viewmodel.ReposViewModel
import com.hbvhuwe.explorergithub.viewmodel.RepositoryViewModel
import com.hbvhuwe.explorergithub.viewmodel.UserViewModel
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
}