(function() {
    'use strict';
    angular
        .module('pliroApp')
        .factory('Payment', Payment);

    Payment.$inject = ['$resource', 'DateUtils'];

    function Payment ($resource, DateUtils) {
        var resourceUrl =  'api/payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paidAt = DateUtils.convertDateTimeFromServer(data.paidAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
