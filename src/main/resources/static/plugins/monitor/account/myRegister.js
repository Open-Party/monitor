/**
 * Created by soarpenguin on 17/10/15.
 */

/*
 *  Document url: http://www.runoob.com/angularjs/angularjs-tutorial.html
*/
angular.module('myRegister', []).controller('validateCtrl', function($scope) {
    $scope.username = '';
    $scope.email = '';
    $scope.password = '';
    $scope.rpassword = '';
    $scope.incomplete = true;

    $scope.$watch('password',function() {$scope.test();});
    $scope.$watch('rpassword',function() {$scope.test();});
    $scope.$watch('username', function() {$scope.test();});
    $scope.$watch('email', function() {$scope.test();});

    $scope.test = function() {
        $scope.incomplete = true;
        if (($scope.isValid($scope.username) &&  $scope.username.length >= 5) &&
            $scope.isValid($scope.email) &&
            ($scope.isValid($scope.password) &&  $scope.password.length >= 6) &&
            $scope.isValid($scope.rpassword)) {
            if (($scope.password !== $scope.rpassword)) {
                $scope.incomplete = true;
            } else {
                $scope.incomplete = false;
            }
        } else {
            $scope.incomplete = true;
        }
    };

    $scope.isValid = function(val) {
        return (val != null) && (val.length > 0);
    }
});