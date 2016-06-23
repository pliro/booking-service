(function() {
    'use strict';

    angular
        .module('pliroApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('verification-code', {
            parent: 'entity',
            url: '/verification-code',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VerificationCodes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/verification-code/verification-codes.html',
                    controller: 'VerificationCodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('verification-code-detail', {
            parent: 'entity',
            url: '/verification-code/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VerificationCode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/verification-code/verification-code-detail.html',
                    controller: 'VerificationCodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'VerificationCode', function($stateParams, VerificationCode) {
                    return VerificationCode.get({id : $stateParams.id});
                }]
            }
        })
        .state('verification-code.new', {
            parent: 'verification-code',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/verification-code/verification-code-dialog.html',
                    controller: 'VerificationCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                verificationCodeId: null,
                                system: null,
                                value: null,
                                code: null,
                                verifiedAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('verification-code', null, { reload: true });
                }, function() {
                    $state.go('verification-code');
                });
            }]
        })
        .state('verification-code.edit', {
            parent: 'verification-code',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/verification-code/verification-code-dialog.html',
                    controller: 'VerificationCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VerificationCode', function(VerificationCode) {
                            return VerificationCode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('verification-code', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('verification-code.delete', {
            parent: 'verification-code',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/verification-code/verification-code-delete-dialog.html',
                    controller: 'VerificationCodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VerificationCode', function(VerificationCode) {
                            return VerificationCode.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('verification-code', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
