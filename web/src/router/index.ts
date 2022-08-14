import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminUser from '../views/admin/AdminUser.vue'
import AboutView from '../views/AboutView.vue'
import AdminEbook from '../views/admin/AdminEbook.vue'
import AdminCategory from '../views/admin/AdminCategory.vue'
import AdminDoc from '../views/admin/AdminDoc.vue'
import DocView from '../views/DocView.vue'
import store from "@/store";
import {Tool} from "@/util/tool";

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
        component: AdminUser,
        // 对页面自定义元数据
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/ebook',
        name: 'AdminEbook',
        component: AdminEbook,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/category',
        name: 'AdminCategory',
        component: AdminCategory,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/doc',
        name: 'AdminDoc',
        component: AdminDoc,
        meta: {
            loginRequire: true
        }
    },

];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// 路由登录拦截，针对每一次的路由跳转
router.beforeEach((to, from, next) => {
    // 要不要对meta.loginRequire属性做监控拦截
    if (to.matched.some(function (item) {
        console.log(item, "是否需要登录校验：", item.meta.loginRequire);
        return item.meta.loginRequire  // 返回true值，if往里走
    })) {
        const loginUser = store.state.user;
        if (Tool.isEmpty(loginUser)) {
            console.log("用户未登录！");
            next('/');
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router
