(function() {
    'use strict';
    angular
        .module('pliroApp')
        .factory('Booking', Booking);

    Booking.$inject = ['$resource', 'DateUtils'];

    function Booking ($resource, DateUtils) {
        var resourceUrl =  'api/bookings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.expiresAt = DateUtils.convertDateTimeFromServer(data.expiresAt);
                        data.bookedFrom = DateUtils.convertDateTimeFromServer(data.bookedFrom);
                        data.bookedTo = DateUtils.convertDateTimeFromServer(data.bookedTo);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
