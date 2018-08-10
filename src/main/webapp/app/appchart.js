'use strict';

var app = angular.module('dashboard-charts', ['googlechart']).value('googleChartApiConfig', {
            version: '1',
            optionalSettings: {
                packages: ['corechart'],
                language: 'ja'
            }
    });

app.config(['$httpProvider', function($httpProvider) {
    //initialize get if not there
    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};    
    }    

    // Answer edited to include suggestions from comments
    // because previous version of code introduced browser-related errors

    //disable IE ajax request caching
    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
    // extra
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
}]);

app.controller("SampleCtrl", function ($scope, $http) {
  
    $scope.init = function () {
     
      $http({
         		method : 'GET',
         		url : 'DataAccessServlet',
         		params : {
         			whatdata : "getChartData"
         		}
         	}).success(function(data, status, headers, config) {
         		$scope.chartdata = data.users;
         		$scope.loginUser = data.loginUser;
         		$scope.networkZnChartData = data.networkZnChartData;
         		$scope.chart = data.users;
         		$scope.incData=data.incidentData;
         	   
         	});
       
    };
    
   $scope.init();
   $scope.selected = function(selectedItem){
      alert("You selected " + $scope.chart.data.cols[selectedItem.column].label + " in " + 
       $scope.chart.data.rows[selectedItem.row].c[0].v);
       
   };

   
  

    $scope.chartReady = function() {
    };
    
    
});


