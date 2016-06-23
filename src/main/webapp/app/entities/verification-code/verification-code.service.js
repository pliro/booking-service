(function() {
    'use strict';
    angular
        .module('pliroApp')
        .factory('VerificationCode', VerificationCode);

    VerificationCode.$inject = ['$resource', 'DateUtils'];

    function VerificationCode ($resource, DateUtils) {
        var resourceUrl =  'api/verification-codes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.verifiedAt = DateUtils.convertDateTimeFromServer(data.verifiedAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
