'use strict';

describe('Controller Tests', function() {

    describe('VerificationCode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVerificationCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVerificationCode = jasmine.createSpy('MockVerificationCode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'VerificationCode': MockVerificationCode
            };
            createController = function() {
                $injector.get('$controller')("VerificationCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pliroApp:verificationCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
