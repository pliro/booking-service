(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('BookingDeleteController',BookingDeleteController);

    BookingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Booking'];

    function BookingDeleteController($uibModalInstance, entity, Booking) {
        var vm = this;
        vm.booking = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Booking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
