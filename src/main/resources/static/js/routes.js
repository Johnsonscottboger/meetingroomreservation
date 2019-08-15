var routes = [{
   path: '/reserve-index',
   name: 'reserve-index',
   component: util.loadComponent('/reserve/index')
}, {
    path: '/user-index',
    name: 'user-index',
    component: util.loadComponent('/user/index')
}, {
    path: '/meetingroom-index',
    name: 'meetingroom-index',
    component: util.loadComponent('/meetingroom/index')
}];

var router = new VueRouter({
    routes: routes
});
