var proba = angular.module('proba', [ngSanitize]);

proba.controller('ProbaCtrl', function($scope, $http, $compile, $sanitize) {

	$scope.model1 = "";
	$scope.model2 = "";
	
	this.metoda1 = function(){
		$http.get('/napraviToken').
	    then(function(response) {
	    	
	    });
	
	}
	
	this.metoda2 = function(){
		$http.get('/testToken').
	    then(function(response) {
	    	
	    });
	
	}
	
	
	this.funkcija = function(){
		$sanitize($scope.model1);
		$scope.model2 = $scope.model1;
	}
	
});