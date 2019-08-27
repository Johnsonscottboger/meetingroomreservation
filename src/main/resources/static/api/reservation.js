/**
 * 会议室预约相关 API
 */

var request = axios;
var reservationApi = {

    /**
     * 获取今天预约记录
     */
    getReservationRecordToady : function () {
        return request.get('/reserve');
    },

    /**
     * 获取指定日期的预约记录
     */
    getReservationRecordByDay: function(date){
        return request.get('/reserve', {
            params: {
                date: date
            }
        })
    },

    /**
     * 预约会议室
     */
    reserve : function (reservationRecord) {
        return request.post('/reserve', reservationRecord);
    },

    /**
     * 更新
     */
    update: function (reservationRecord) {
        return request.post('/reserve', reservationRecord);
    }
};