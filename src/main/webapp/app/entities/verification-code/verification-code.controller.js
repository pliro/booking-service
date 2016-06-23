(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('VerificationCodeController', VerificationCodeController);

    VerificationCodeController.$inject = ['$scope', '$state', 'VerificationCode', 'ParseLinks', 'AlertService'];

    function VerificationCodeController ($scope, $state, VerificationCode, ParseLinks, AlertService) {
        var vm = this;
        vm.verificationCodes = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            VerificationCode.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.verificationCodes.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.verificationCodes = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
