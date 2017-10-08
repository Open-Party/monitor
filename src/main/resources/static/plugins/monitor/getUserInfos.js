/**
 * Created by soarpenguin on 17/10/8.
 */

//获取用户个人信息、owner的应用列表以及用户关注的应用列表

function getUserInfo() {
    var mydata ;
    $.ajaxSetup({async:false});
    $.get("/account/getUserAllInfo", function(data){
        mydata = data;
    });
    $.ajaxSetup({async:true});
    return mydata;
}

var userInfo = getUserInfo();

var monitorModule = angular.module('monitor', []);

monitorModule.controller('monitorController', ['$scope','$http','$window', function($scope,$http,$window) {
    $scope.currentUser = userInfo.currentUser;
    $scope.isAdmin = userInfo.isAdmin;
    // $scope.isInspectInfo = true;
    $scope.changeApp = function () {
        // console.log(appName);
        // $http.get('/appinfo/appDetail?appname='+ appName).success(function (data) {
        // 	// console.log(data);
        // 	return data;
        // });
        // $window.location.href = '/appinfo/appDetail?appname='+ appName;
        // $scope.currentApp = appName;
    };
}]);

