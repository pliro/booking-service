(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('VerificationCodeDialogController', VerificationCodeDialogController);

    VerificationCodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VerificationCode'];

    function VerificationCodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VerificationCode) {
        var vm = this;
        vm.verificationCode = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('pliroApp:verificationCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.verificationCode.id !== null) {
                VerificationCode.update(vm.verificationCode, onSaveSuccess, onSaveError);
            } else {
                VerificationCode.save(vm.verificationCode, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.verifiedAt = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
