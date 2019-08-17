/**
 * 用户相关 API
 */

var request = axios;
var userApi = {

    /**
     * 获取所有用户
     */
    getUserList: function () {
        return request.get('/user');
    },

    /**
     * 获取指定用户
     */
    getUser:function (id) {
        return request.get('/user/', {
            params :{
                id : id
            }
        });
    },

    /**
     * 查找用户
     */
    searchUser: function (params) {
        return request.get('/user', {
            params : params
        });
    },

    /**
     * 删除用户
     */
    deleteUser: function (user) {
        return request.delete('/user', {
            params: user
        });
    },

    /**
     * 导入用户
     */
    importUsers: function (users) {
        return request.post('/user/import', users);
    }
};