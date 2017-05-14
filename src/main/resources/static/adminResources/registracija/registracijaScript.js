


administratorBanke.controller('RukovanjeKorisnicima', function($scope, $http, $compile){
	
	$scope.lozinka1 = "";
	$scope.banka = {};
	$scope.rola = {};
	
	$scope.rezim = 0;
	//0 za pregled, 1 za dodavanje, 2 za pretragu
	
	$scope.korisnici = [];
	$scope.korisnik = {};

	
	$scope.promenjen = false;
	
	$scope.idSelektovanogKorisnika = null;
	
	$scope.init = function(){
		
		$http.get('/allUsers').
        then(function(response) {
        	$scope.korisnici = response.data;
        	
        });
		
		$http.get('/allRoles').
        then(function(response) {
        	$scope.sveRole = response.data;
        	
        });
		
		$http.get('/allBanks').
        then(function(response) {
        	$scope.sveBanke = response.data;
        	
        });
		
	};
	
	this.nadjiBanke = function(){
		$http.get('/allBanks').
        then(function(response) {
        	$scope.sveBanke = response.data;
        	
        });
	};
	
	this.nadjiRole = function(){
		$http.get('/allRoles').
        then(function(response) {
        	$scope.sveRole = response.data;
        	
        });
		
	};
	
	this.refresh = function(){
		
		$http.get('/allUsers').
        then(function(response) {
        	$scope.korisnici = response.data;
        	
        });
		
	}
	
	this.addClick = function(){
		
		$scope.rezim =1;
		$scope.korisnik = {};
		$scope.banka = {};
		$scope.rola = {};
	};
	
	this.searchClick = function(){
		$scope.rezim =2;
		$scope.korisnik = {};
		$scope.banka = {};
		$scope.rola = {};
	};
	
	this.commitClick = function(){
		
		
	
		if(angular.equals($scope.rezim, 1)){
			
			$scope.rezim = 1;
			
			if(angular.equals($scope.korisnik, {})){
				
				return;
			}else if(angular.isUndefined($scope.korisnik.name)){
				toastr.error('Ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.surname)){
				toastr.error('Prezime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.username)){
				toastr.error('Korisnicko ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.email)){
				toastr.error('Email adresa mora biti zadata!');
				return;
			}
//			}else if(angular.isUndefined($scope.korisnik.password)){
//				toastr.error('Lozinka mora biti zadata!');
//				return;
//			}
			else if(angular.isUndefined($scope.banka)){
				toastr.error('Banka mora biti zadata!');
				return;
			}else if(angular.isUndefined($scope.rola)){
				toastr.error('Rola mora biti zadata!');
				return;
			}
			
			$scope.korisnik.bank = angular.copy($scope.banka);
			$scope.korisnik.role = angular.copy($scope.rola);
			
			
			$http({
    		    method: 'POST',
    		    url: '/newUser',
    		    data: $scope.korisnik
    		}).
    		then(function mySucces(response) {
    				
    				if(response.data.id == -1){
    					toastr.error('Neuspesan unos!');
    					return;
    				}
    			
    				if(!angular.isArray($scope.korisnici)){
    					$scope.korisnici = [];
    				}
    				
    				$scope.korisnici.push(response.data);
    				
    				$scope.korisnik = {};
    				$scope.banka = {};
    				$scope.rola = {};
    				$scope.promenjen = false;
    		});
      
		}else if(angular.equals($scope.rezim, 0) & !angular.equals($scope.korisnik, {}) & !angular.equals($scope.idSelektovanePermisije, null)){
			
			$scope.rezim = 0;
			

			if(angular.equals($scope.korisnik, {})){
				
				return;
			}else if(angular.isUndefined($scope.korisnik.name)){
				toastr.error('Ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.surname)){
				toastr.error('Prezime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.username)){
				toastr.error('Korisnicko ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.korisnik.email)){
				toastr.error('Email adresa mora biti zadata!');
				return;
			}
//			else if(angular.isUndefined($scope.korisnik.password)){
//				toastr.error('Lozinka mora biti zadata!');
//				return;
//			}
			else if(angular.isUndefined($scope.banka)){
				toastr.error('Banka mora biti zadata!');
				return;
			}else if(angular.isUndefined($scope.rola)){
				toastr.error('Rola mora biti zadata!');
				return;
			}
			
			$scope.korisnik.bank = angular.copy($scope.banka);
			$scope.korisnik.role = angular.copy($scope.rola);
			
			
			$http({
    		    method: 'POST',
    		    url: '/updateUser',
    		    data: $scope.korisnik
    		}).
    		then(function mySucces(response) {
    			
	    			if(response.data.id == -1){
						toastr.error('Neuspesan unos!');
						return;
					}
    			
    				var temp = -1;
	    			for (var i = 0; i < $scope.korisnici.length; i++) { 
					    if(angular.equals($scope.korisnici[i].id, response.data.id)){
					    	temp = i;
					    	break;
					    }
					}
					
    				if(!angular.equals(temp, -1)){
    					$scope.korisnici[i] = response.data;
    					
    				}
    				
    				$scope.korisnik = {};
    				$scope.banka = {};
    				$scope.rola = {};
    				$scope.promenjen = false;
    		});
			
		}
		
		
	};

	this.rollbackClick = function(){
		
		if(angular.equals($scope.rezim, 1) || angular.equals($scope.rezim, 2)){
			$scope.rezim = 0;
			$scope.korisnik = {};
		}else{
			$scope.rezim = 0;
			
			$scope.korisnik = {};
			$scope.banka = {};
			$scope.rola = {};
		}
		$scope.promenjen = false;
	};
	
	
	
	this.deleteClick= function(){
		
		if(angular.equals($scope.korisnik, {})){
			
			return;
		}
		
		$http.delete('/deleteUser/'+$scope.korisnik.id).
        then(function(response) {
        	
        	if(response.data.id == -1){
				toastr.error('Neuspesno brisanje! Moguce je da je odabrana stavka povezana sa drugim stavkama te ju je nemoguce obrisati.');
				return;
			}
        	
        	var temp = -1;
    		for (var i = 0; i < $scope.korisnici.length; i++) { 
    		    if(angular.equals($scope.korisnici[i].id, $scope.korisnik.id)){
    		    	temp = i;
    		    	break;
    		    }
    		}
    		
    		if(!angular.equals(temp, -1)){
    			$scope.korisnici.splice(temp, 1);
    		}
    		
    		$scope.korisnik = {};
    		$scope.banka = {};
    		$scope.rola = {};
    		$scope.promenjen = false;
        });
		
		
	}
	
	$scope.nultoStanje = function(){
		if(angular.equals($scope.rezim, 0)){
			return true;
		}else{
			return false;
		}
	}
	
	this.firstClick = function(){
		
		if(!$scope.nultoStanje()){
			return;
		}
		
		$scope.korisnik = angular.copy($scope.korisnici[0]);
		$scope.idSelektovanogKorisnika = $scope.korisnici[0].id;
		$scope.banka = angular.copy($scope.korisnik.bank);
		$scope.rola = angular.copy($scope.korisnik.role);
		$scope.promenjen = false;
	};

	this.prevClick = function(){
		
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.korisnici.length; i++) { 
		    if(angular.equals($scope.korisnici[i].id, $scope.korisnik.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == 0 & temp!=-1){
			return;
		}
		
		$scope.korisnik = angular.copy($scope.korisnici[temp-1]);
		$scope.idSelektovanogKorisnika = $scope.korisnici[temp-1].id;
		$scope.banka = angular.copy($scope.korisnik.bank);
		$scope.rola = angular.copy($scope.korisnik.role);
		$scope.promenjen = false;
	};
	

	this.nextClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.korisnici.length; i++) { 
		    if(angular.equals($scope.korisnici[i].id, $scope.korisnik.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == $scope.korisnici.length-1 & temp!=-1){
			return;
		}
		
		$scope.korisnik = angular.copy($scope.korisnici[temp+1]);
		$scope.idSelektovanogKorisnika = $scope.korisnici[temp+1].id;
		$scope.banka = angular.copy($scope.korisnik.bank);
		$scope.rola = angular.copy($scope.korisnik.role);
		$scope.promenjen = false;
	};
	

	this.lastClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		$scope.korisnik = angular.copy($scope.korisnici[$scope.korisnici.length-1]);
		$scope.idSelektovanogKorisnika = $scope.korisnici[$scope.korisnici.length-1].id;
		$scope.banka = angular.copy($scope.korisnik.bank);
		$scope.rola = angular.copy($scope.korisnik.role);
		$scope.promenjen = false;
	};
	
	
	this.setSelected = function(d){
		if(angular.equals($scope.rezim, 0)){
			$scope.promenjen = false;
			$scope.idSelektovanogKorisnika = d.id;
			$scope.korisnik = angular.copy(d);
			$scope.banka = angular.copy(d.bank);
			$scope.rola = angular.copy(d.role);
		}
	};
	
	this.promenjen = function(){
		if(angular.equals($scope.korisnik, {})){
			return;
		}
		$scope.promenjen = true;
	}
	
	this.prazanKorisnik = function(){
		if(angular.equals($scope.korisnik, {})){
			return true;
		}else{
			return false;
		}
	}
	
	this.setSelectedBank = function(b){
		if(angular.equals($scope.rezim, 0)){
			if(angular.equals($scope.korisnik, {})){
				return;
			}
		}
		
		$scope.banka = b;
		
	};
	
	this.setSelectedRole = function(r){
		if(angular.equals($scope.rezim, 0)){
			if(angular.equals($scope.korisnik, {})){
				return;
			}
		}
		
		$scope.rola = r;
		
	};
	
	this.dismiss = function(){
		if(angular.equals($scope.rezim, 0)){
			if(angular.equals($scope.korisnik, {})){
				return;
			}else{
				$scope.banka = angular.copy($scope.korisnik.bank);
			}
		}
	};
	
	this.dismissRole = function(){
		if(angular.equals($scope.rezim, 0)){
			if(angular.equals($scope.korisnik, {})){
				return;
			}else{
				$scope.rola = angular.copy($scope.korisnik.role);
			}
		}
	};
	
	
	
});

administratorBanke.directive('ngConfirmClick', [
    function(){
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Da li ste sigurni?";
                var clickAction = attr.confirmedClick;
                element.bind('click',function (event) {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction)
                    }
                });
            }
        };
}])

administratorBanke.directive("passwordVerify", function() {
	   return {
		      require: "ngModel",
		      scope: {
		        passwordVerify: '='
		      },
		      link: function(scope, element, attrs, ctrl) {
		        scope.$watch(function() {
		            var combined;

		            if (scope.passwordVerify || ctrl.$viewValue) {
		               combined = scope.passwordVerify + '_' + ctrl.$viewValue; 
		            }                    
		            return combined;
		        }, function(value) {
		            if (value) {
		                ctrl.$parsers.unshift(function(viewValue) {
		                    var origin = scope.passwordVerify;
		                    if (origin !== viewValue) {
		                        ctrl.$setValidity("passwordVerify", false);
		                        return undefined;
		                    } else {
		                        ctrl.$setValidity("passwordVerify", true);
		                        return viewValue;
		                    }
		                });
		            }
		        });
		     }
		   };
		});

administratorBanke.filter('stringRezima', function() {
    return function(input) {
    	
    	if(angular.equals(input, 0)){
    		return "pregled/izmenu.";
    	}
    	else if(angular.equals(input, 1)){
    		return "dodavanje.";
    	}
    	else{
    		return "pretragu.";
    	}
       
    }
	
	
	
});