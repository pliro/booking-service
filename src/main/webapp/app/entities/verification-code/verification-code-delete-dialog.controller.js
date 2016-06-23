(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('VerificationCodeDeleteController',VerificationCodeDeleteController);

    VerificationCodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'VerificationCode'];

    function VerificationCodeDeleteController($uibModalInstance, entity, VerificationCode) {
        var vm = this;
        vm.verificationCode = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            VerificationCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
