/**
 * 会议室相关 API
 */

var request = axios;
var meetingroomApi = {
    /**
     * 获取所有会议室
     */
    getMeetingRoomList: function () {
        return request.get('/meetingroom');
    },

    /**
     * 获取指定会议室
     */
    getMeetingRoom: function (id) {
        return request.get('/meetingroom/', {
            params: {
                id: id
            }
        });
    },

    /**
     * 添加会议室
     */
    addMeetingRoom: function (meetingRoom) {
        return request.post('/meetingroom', meetingRoom);
    },

    /**
     * 更新会议室
     */
    updateMeetingRoom: function (meetingRoom) {
        return request.put('/meetingroom', {
            meetingRoom: meetingRoom
        })
    },

    /**
     * 删除会议室
     */
    deleteMeetingRoom: function (meetingRoom) {
        return request.delete('/meetingroom', {
            params: meetingRoom
        });
    }
};