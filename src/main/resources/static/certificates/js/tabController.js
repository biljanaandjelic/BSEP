certificateModule.controller("TabController", function($scope, $http, $compile, $timeout, $rootScope, $window, tokenService){
    this.tab = 8;

    $scope.init = function(){
	
		
		$http.get('/special/getSafeToken').
		then(function mySucces(response) {
			
			
			if(angular.equals(response.data.name, 'OHNO')){
				$window.location.href=response.data.value;
			}
			
			tokenService.setToken(response.data.value);
		});
    };
    
    
    
    this.isSet = function(checkTab) {
      return this.tab === checkTab;
    };

    this.setTab = function(setTab) {
      this.tab = setTab;
    };
    
    this.closeKeystore = function(){
    	$http.get('/certificates/closeKeystore').
        then(function(response) {
        	if(response.data.id == 1){
        		$window.location.href=response.data.url;
				
				return;
        	}
        });
    };
});