import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人中心' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'doctor', meta: { name: '医生信息' }, component: () => import('@/views/manager/Doctor.vue'), },
        { path: 'department', meta: { name: '科室信息' }, component: () => import('@/views/manager/Department.vue'), },
        { path: 'title', meta: { name: '职称信息' }, component: () => import('@/views/manager/Title.vue'), },
        { path: 'user', meta: { name: '用户信息' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'science', meta: { name: '健康资讯' }, component: () => import('@/views/manager/Science.vue'), },
        { path: 'booking', meta: { name: '在线预约' }, component: () => import('@/views/manager/Booking.vue'), },
        { path: 'examinationOrder', meta: { name: '预约订单' }, component: () => import('@/views/manager/ExaminationOrder.vue'), },
        { path: 'data', meta: { name: '数据统计' }, component: () => import('@/views/manager/Data.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        // { path: 'person', component: () => import('@/views/front/Person.vue'),  },
        { path: 'department', meta: { name: '科室信息' }, component: () => import('@/views/front/Department.vue'), },
        { path: 'doctor', meta: { name: '医生信息' }, component: () => import('@/views/front/Doctor.vue'), },
        { path: 'person', meta: { name: '个人中心' }, component: () => import('@/views/front/Person.vue'), },
        { path: 'science', meta: { name: '健康资讯' }, component: () => import('@/views/front/Science.vue'), },
        { path: 'scienceDetail', meta: { name: '健康资讯细节' }, component: () => import('@/views/front/ScienceDetail.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/front/Password.vue'), },
        { path: 'userBooking', meta: { name: '用户在线预约' }, component: () => import('@/views/front/UserBooking.vue'), },
        { path: 'examinationOrder', meta: { name: '历史预约订单' }, component: () => import('@/views/front/ExaminationOrder.vue'), },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router
