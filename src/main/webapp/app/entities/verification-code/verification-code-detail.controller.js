(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('VerificationCodeDetailController', VerificationCodeDetailController);

    VerificationCodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'VerificationCode'];

    function VerificationCodeDetailController($scope, $rootScope, $stateParams, entity, VerificationCode) {
        var vm = this;
        vm.verificationCode = entity;
        
        var unsubscribe = $rootScope.$on('pliroApp:verificationCodeUpdate', function(event, result) {
            vm.verificationCode = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
