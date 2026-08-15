package com.motoristaapp.financas.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.motoristaapp.financas.data.repository.AppContainer
import com.motoristaapp.financas.ui.dashboard.DashboardScreen
import com.motoristaapp.financas.ui.dashboard.DashboardViewModel
import com.motoristaapp.financas.ui.earnings.AddEarningScreen
import com.motoristaapp.financas.ui.earnings.EarningsScreen
import com.motoristaapp.financas.ui.earnings.EarningsViewModel
import com.motoristaapp.financas.ui.expenses.AddExpenseScreen
import com.motoristaapp.financas.ui.expenses.ExpensesScreen
import com.motoristaapp.financas.ui.expenses.ExpensesViewModel
import com.motoristaapp.financas.ui.goals.GoalsScreen
import com.motoristaapp.financas.ui.goals.GoalsViewModel

sealed class Screen(val route: String, val label: String) {
    data object Dashboard : Screen("dashboard", "Início")
    data object Earnings : Screen("earnings", "Ganhos")
    data object AddEarning : Screen("earnings/add", "Adicionar ganho")
    data object Expenses : Screen("expenses", "Despesas")
    data object AddExpense : Screen("expenses/add", "Adicionar despesa")
    data object Goals : Screen("goals", "Metas")
    data object Reports : Screen("reports", "Relatórios")
}

private val bottomItems = listOf(Screen.Dashboard, Screen.Earnings, Screen.Expenses, Screen.Goals, Screen.Reports)

private fun iconFor(route: String) = when (route) {
    Screen.Dashboard.route -> Icons.Filled.Home
    Screen.Earnings.route -> Icons.Filled.AttachMoney
    Screen.Expenses.route -> Icons.Filled.MoneyOff
    Screen.Goals.route -> Icons.Filled.Flag
    else -> Icons.Filled.BarChart
}

@Composable
fun AppNavHost(container: AppContainer) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                bottomItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(iconFor(screen.route), contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable(Screen.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(factory = viewModelFactory {
                    DashboardViewModel(container.earningRepository, container.expenseRepository, container.goalRepository)
                })
                DashboardScreen(vm)
            }
            composable(Screen.Earnings.route) {
                val vm: EarningsViewModel = viewModel(factory = viewModelFactory { EarningsViewModel(container.earningRepository) })
                EarningsScreen(vm, onAddClick = { navController.navigate(Screen.AddEarning.route) })
            }
            composable(Screen.AddEarning.route) {
                val vm: EarningsViewModel = viewModel(factory = viewModelFactory { EarningsViewModel(container.earningRepository) })
                AddEarningScreen(vm, onBack = { navController.popBackStack() })
            }
            composable(Screen.Expenses.route) {
                val vm: ExpensesViewModel = viewModel(factory = viewModelFactory { ExpensesViewModel(container.expenseRepository) })
                ExpensesScreen(vm, onAddClick = { navController.navigate(Screen.AddExpense.route) })
            }
            composable(Screen.AddExpense.route) {
                val vm: ExpensesViewModel = viewModel(factory = viewModelFactory { ExpensesViewModel(container.expenseRepository) })
                AddExpenseScreen(vm, onBack = { navController.popBackStack() })
            }
            composable(Screen.Goals.route) {
                val vm: GoalsViewModel = viewModel(factory = viewModelFactory {
                    GoalsViewModel(container.goalRepository, container.earningRepository, container.expenseRepository)
                })
                GoalsScreen(vm)
            }
            composable(Screen.Reports.route) {
                com.motoristaapp.financas.ui.reports.ReportsPlaceholderScreen()
            }
        }
    }
}
