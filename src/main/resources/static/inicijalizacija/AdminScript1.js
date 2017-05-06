administrator.controller('Opsti', function($scope, $http, $compile, $timeout, $rootScope){
	
	$scope.tab = 0;
	
	$scope.dr = -1;
	
	
	
	$scope.init = function(){
	
		
	};
	

	this.tabClick = function(num){
		
		$scope.tab = num;
		
	};
	
	this.tabClick2 = function(num, drzava){
		
		$scope.tab = num;
		
		if(angular.equals(drzava, {})){
			return;
		}
		
		$scope.dr = drzava.id;
		
		$scope.$broadcast('filterPoDrzavi', drzava.id); // going down!
	};
	
});