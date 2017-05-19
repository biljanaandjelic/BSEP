administrator.controller('Opsti', function($scope, $http, $compile, $timeout, $rootScope){
	
	$scope.tab = 0;
	
	$scope.dr = -1;
	
	$scope.nm = -1;
	
	$scope.kl = -1;
	
	$scope.permisijeIzPermisija = [];
	
	$scope.init = function(){
	
		
	};
	
	this.logoff = function(){
		
		$http.get('/logoff').
		then(function mySucces(response) {
			
				//$window.location.href="http://localhost:8080/authentification/login.html";
				toastr.success("IZLOGOVAN");
		});
	};

	this.tabClick = function(num){
		
		$scope.tab = num;
		
	};
	
	this.novoZatvaranje = function(){
		$scope.$broadcast('novoZatvaranje'); // going down!
	};
	
	this.tabClick2 = function(num, drzava){
		
		$scope.tab = num;
		
		if(angular.equals(drzava, {})){
			return;
		}
		
		$scope.dr = drzava.id;
		
		$scope.$broadcast('filterPoDrzavi', drzava.id); // going down!
	};
	
	this.tabClick5 = function(num, naseljenoMesto){
		
		$scope.tab = num;
		
		if(angular.equals(naseljenoMesto, {})){
			return;
		}
		
		$scope.nm = naseljenoMesto.id;
		
		$scope.$broadcast('filterPoNaseljenomMestuKlijent', naseljenoMesto.id); // going down!
	};
	
	this.tabClick6 = function(num, naseljenoMesto){
		
		$scope.tab = num;
		
		if(angular.equals(naseljenoMesto, {})){
			return;
		}
		
		$scope.nm = naseljenoMesto.id;
		
		$scope.$broadcast('filterPoNaseljenomMestuPravnoLice', naseljenoMesto.id); // going down!
	};
	
	this.tabClick7 = function(num, klijent){
		
		$scope.tab = num;
		
		if(angular.equals(klijent, {})){
			return;
		}
		
		$scope.kl = klijent.id;
		
		$scope.$broadcast('filterPoKlijentuRacun', klijent.id); // going down!
	};
	
	this.tabClick8 = function(num, idSelektovanogRacuna){
		$scope.tab = num;
		$scope.$broadcast('filterZatvaranja', idSelektovanogRacuna); // going down!
	};
});