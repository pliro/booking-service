(function() {
    'use strict';

    angular
        .module('pliroApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('booking', {
            parent: 'entity',
            url: '/booking',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bookings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/bookings.html',
                    controller: 'BookingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('booking-detail', {
            parent: 'entity',
            url: '/booking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Booking'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking/booking-detail.html',
                    controller: 'BookingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Booking', function($stateParams, Booking) {
                    return Booking.get({id : $stateParams.id});
                }]
            }
        })
        .state('booking.new', {
            parent: 'booking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bookingId: null,
                                expiresAt: null,
                                state: null,
                                slotId: null,
                                bookedFrom: null,
                                bookedTo: null,
                                resourceId: null,
                                appointmentId: null,
                                active: null,
                                notes: null,
                                personId: null,
                                verificationCodeId: null,
                                paymentId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('booking');
                });
            }]
        })
        .state('booking.edit', {
            parent: 'booking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-dialog.html',
                    controller: 'BookingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking.delete', {
            parent: 'booking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking/booking-delete-dialog.html',
                    controller: 'BookingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Booking', function(Booking) {
                            return Booking.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
