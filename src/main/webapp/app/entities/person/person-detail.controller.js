(function() {
    'use strict';

    angular
        .module('pliroApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person) {
        var vm = this;
        vm.person = entity;
        
        var unsubscribe = $rootScope.$on('pliroApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
