import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminUser from '../views/admin/AdminUser.vue'
import AboutView from '../views/AboutView.vue'
import AdminEbook from '../views/admin/AdminEbook.vue'
import AdminCategory from '../views/admin/AdminCategory.vue'
import AdminDoc from '../views/admin/AdminDoc.vue'
import DocView from '../views/DocView.vue'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/about',
        name: 'about',
        component: AboutView
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        //
    },
    {
        path: '/doc',
        name: 'doc',
        component: DocView
    },
    {
        path: '/admin/user',
        name: 'user',
        component: AdminUser
    },
    {
        path: '/admin/ebook',
        name: 'AdminEbook',
        component: AdminEbook
    },
    {
        path: '/admin/category',
        name: 'AdminCategory',
        component: AdminCategory
    },
    {
        path: '/admin/doc',
        name: 'AdminDoc',
        component: AdminDoc
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
