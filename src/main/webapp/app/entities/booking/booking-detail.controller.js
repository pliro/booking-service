(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('BookingDetailController', BookingDetailController);

    BookingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Booking'];

    function BookingDetailController($scope, $rootScope, $stateParams, entity, Booking) {
        var vm = this;
        vm.booking = entity;
        
        var unsubscribe = $rootScope.$on('pliroApp:bookingUpdate', function(event, result) {
            vm.booking = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
