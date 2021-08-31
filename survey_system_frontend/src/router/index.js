import Vue from 'vue'
import Router from 'vue-router'
import Login from "../components/sys/login";
import Register from "../components/sys/register";
import Home from "../components/home";
import Error404 from "../components/error/404";
import Dashboard from "../components/dashboard/dashboard";
import UserList from "../components/dashboard/content/userList";
import SurveyList from "../components/dashboard/content/surveyList";
import Password from "../components/dashboard/content/password";
import Welcome from "../components/dashboard/content/welcome";
import MySurveyList from "../components/dashboard/content/mySurvey";
import Detail from "../components/participation/surveyBoard";

Vue.use(Router)

var router = new Router({
  routes: [
    {
      path: '/',
      name: "/",
      component: Home
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/detail/:id',
      name: 'detail',
      component: Detail
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path:'/dashboard',
      name: 'dashboard',
      component: Dashboard,
      children: [
        {
          path: '/welcome',
          name: 'welcome',
          component: Welcome
        },
        {
          path: '/user',
          name: 'userList',
          component: UserList
        },
        {
          path: '/survey',
          name: 'surveyList',
          component: SurveyList
        },
        {
          path: '/mySurvey',
          name: 'mySurvey',
          component: MySurveyList
        },
        {
          path: '/password',
          name: 'password-management',
          component: Password
        }
      ]
    },
    {
      path: '/*',
      name: '404',
      component: Error404
    }
  ]
});

export default router;
