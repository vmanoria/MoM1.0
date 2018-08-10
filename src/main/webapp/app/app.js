var app = angular.module('resilientMomApp', ['angularUtils.directives.dirPagination']);

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

app.controller('listdata',function($scope, $http,$interval){
	$scope.users = []; //declare an empty array
	
	$scope.referehInterval = 5000000;
	var time = $scope.referehInterval;
	$http({
		method : 'GET',
		url : 'DataAccessServlet',
		params : {
			whatdata : "getIncidentList"
		}
	}).success(function(data, status, headers, config) {
		var userList = data.users;
		$scope.users = data.users;
		$scope.allIncident = data.users;
		$scope.orgs = data.orgname;
		$scope.loginUser = data.loginUser;
		$scope.isOnlyOneOrg=data.isOnlyOneOrg;
		$scope.headerincident=data.headerincident;
		$scope.sortKey='id';
		$scope.selection=[];
		$scope.referehInterval = data.referehInterval;
		time = $scope.referehInterval;
		$scope.selectedorgs =  new Array($scope.orgs.length);
	   
	});		
		
	
	/*$http.get("mockJson/mock.json").success(function(response){ 
		$scope.users = response;  //ajax request to fetch data into $scope.data
		alert("mock json");
	});*/
	
	$scope.sort = function(keyname){
		$scope.sortKey = keyname;   //set the sortKey to the param passed
		$scope.reverse = !$scope.reverse; //if true make it false and vice versa
	}
	 $scope.refresh = function() {	 
	     $http({
			method : 'GET',
			url : 'DataAccessServlet',
			params : {
				whatdata : "getIncidentList"
			}
		}).success(function(data, status, headers, config) {
			var userList = data.users;
			$scope.users = data.users;
			$scope.sortKey='id';
			$scope.allIncident = data.users;
			$scope.isOnlyOneOrg=data.isOnlyOneOrg;
			fetchFilterOrgData();
				   
		});    
	 }
	$interval(function(){
		$http({
			method : 'GET',
			url : 'DataAccessServlet',
			params : {
				whatdata : "getIncidentList"
			}
		}).success(function(data, status, headers, config) {
			var userList = data.users;
			$scope.users = data.users;
			$scope.allIncident = data.users;
			$scope.isOnlyOneOrg=data.isOnlyOneOrg;
			$scope.sortKey='id';
			$scope.referehInterval = data.referehInterval;
			
		    fetchFilterOrgData()
		});	
	},time);
	
	$scope.getSelectedOrg = function() {
		
    	$scope.output = JSON.stringify(getSelected());
    	
    	
    	
    	
    }
    
      var childWindow ;
               
    $scope.openChildWindow = function(incurl,orgurl,incidentId,orgName) {         			
                   
        childWindow = window.open(orgurl);
        
        alert("Opening Incident details of "+incidentId+" from Organization  "+orgName);                      
        childWindow.location.href= incurl;                        
                    
   }
   
                        
    
    function getSelected() {
    	return $scope.orgs.filter(function (x,i) {
    		//alert($scope.selectedorgs[i]);
    		return $scope.selectedorgs[i];
        });
    }
    
    $scope.selection=[];

    $scope.toggleSelection = function toggleSelection(org) {
      var idx = $scope.selection.indexOf(org);
     
      if (idx > -1) {
        // is currently selected
        $scope.selection.splice(idx, 1);
       }
       else {
         // is newly selected
         $scope.selection.push(org);
       }
      fetchFilterOrgData();
    };
    
    function fetchFilterOrgData() {
    	if($scope.selection.length > 0){
    		var accounting = [];
	    	for(var i = 0; i < $scope.allIncident.length; i++) {
	    	    var obj = $scope.allIncident[i];    	   
	    	    for(var orgloop = 0; orgloop < $scope.selection.length; orgloop++) {
	    	    	if($scope.selection[orgloop]==obj.oraganisation){
	    	    		accounting.push(obj);
	    	    	}
	    	    }
	    	    console.log(obj.id);
	    	} 
	    	$scope.users = accounting;
	    }
    }
});


