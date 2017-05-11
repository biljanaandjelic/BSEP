administrator.controller('RukovanjePravnimLicima', function($scope, $http, $compile){
	
	$scope.rezim = 0;
	//0 za pregled, 1 za dodavanje, 2 za pretragu
	
	$scope.pravnoLice = {};
	$scope.pravnaLica = {};
	$scope.svaNaseljenaMesta = {};

	
	$scope.sakrijBrowse = false;
	
	$scope.$on('filterPoNaseljenomMestuPravnoLice', function (event, id) {
	    console.log(id); // Index naseljenog mesta
	    
	    $http.get('http://localhost:8080/nadjiPravnaLica/'+id).
        then(function(response) {
        	$scope.pravnaLica = response.data;
        	$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnaLica[0].naseljenoMesto);
        	$scope.sakrijBrowse = true;
        });
	    
	  });
	
	$scope.idSelektovanogPravnogLica = null;
	
	$scope.selektovanoNaseljenoMesto = {};
	
	$scope.init = function(){
		
		$http.get('http://localhost:8080/svaNaseljenaMesta').
        then(function(response) {
        	$scope.svaNaseljenaMesta = response.data;
        	
        });
		
		$http.get('http://localhost:8080/svaPravnaLica').
        then(function(response) {
        	$scope.pravnaLica = response.data;
        	
        });
		
		
	};
	
	
	this.refresh = function(){
		
		$scope.sakrijBrowse = false;
		
		$http.get('http://localhost:8080/svaPravnaLica').
        then(function(response) {
        	$scope.pravnaLica = response.data;
        	
        });
	}
	
	this.addClick = function(){
		
		$scope.rezim =1;
		$scope.pravnoLice = {};
		$scope.selektovanoNaseljenoMesto = {};
	};
	
	this.searchClick = function(){
		$scope.rezim =2;
		$scope.pravnoLice = {};
		//if(!sakrijBrowse){
			$scope.selektovanoNaseljenoMesto = {};
		//}
	};
	
	this.commitClick = function(){
		
	
		if(angular.equals($scope.rezim, 1)){
			
			$scope.rezim = 1;
			
			if(angular.equals($scope.pravnoLice, {})){
				
				return;
			}else if(angular.isUndefined($scope.pravnoLice.jmbg)){
				toastr.error('Jmbg mora da ima tacno 13 karaktera!');
				return;
			}else if(!angular.equals($scope.pravnoLice.jmbg.trim().length, 13)){
				toastr.error('Jmbg mora da ima tacno 13 karaktera!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.ime)){
				toastr.error('Ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.prezime)){
				toastr.error('Prezime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.adresa)){
				toastr.error('Adresa mora biti zadata!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.telefon)){
				toastr.error('Telefon mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.email)){
				toastr.error('Email mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.pib)){
				toastr.error('Pib mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.fax)){
				toastr.error('Fax mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.odobrio)){
				toastr.error('Polje "Odobrio" mora biti zadato!');
				return;
			}else if(angular.equals($scope.selektovanoNaseljenoMesto, {})){
				toastr.error('Naseljeno mesto mora biti zadato!');
				return;
			}
			
			$scope.pravnoLice.naseljenoMesto = angular.copy($scope.selektovanoNaseljenoMesto);
			
			$http({
    		    method: 'POST',
    		    url: 'http://localhost:8080/novoPravnoLice',
    		    data: $scope.pravnoLice
    		}).
    		then(function mySucces(response) {
    				
    				if(response.data.id == -1){
    					toastr.error('Neuspesan unos!');
    					return;
    				}
    			
    				$scope.pravnaLica.push(response.data);
    				$scope.pravnoLice = {};
    				$scope.selektovanoNaseljenoMesto = {};
    		});
      
		}else if(angular.equals($scope.rezim, 0) & !angular.equals($scope.pravnoLice, {}) & !angular.equals($scope.idSelektovanogPravnogLica, null)){
			
			$scope.rezim = 0;
			

			if(angular.equals($scope.pravnoLice, {})){
				
				return;
			}else if(angular.isUndefined($scope.pravnoLice.jmbg)){
				toastr.error('Jmbg mora da ima tacno 13 karaktera!');
				return;
			}else if(!angular.equals($scope.pravnoLice.jmbg.trim().length, 13)){
				toastr.error('Jmbg mora da ima tacno 13 karaktera!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.ime)){
				toastr.error('Ime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.prezime)){
				toastr.error('Prezime mora biti zadato!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.adresa)){
				toastr.error('Adresa mora biti zadata!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.telefon)){
				toastr.error('Telefon mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.email)){
				toastr.error('Email mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.pib)){
				toastr.error('Pib mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.fax)){
				toastr.error('Fax mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.pravnoLice.odobrio)){
				toastr.error('Polje "Odobrio" mora biti zadato!');
				return;
			}else if(angular.equals($scope.selektovanoNaseljenoMesto, {})){
				toastr.error('Naseljeno mesto mora biti zadato!');
				return;
			}
			
			$scope.pravnoLice.naseljenoMesto = angular.copy($scope.selektovanoNaseljenoMesto);
			
			$http({
    		    method: 'POST',
    		    url: 'http://localhost:8080/azurirajPravnoLice',
    		    data: $scope.pravnoLice
    		}).
    		then(function mySucces(response) {
    			
	    			if(response.data.id == -1){
						toastr.error('Neuspesan unos!');
						return;
					}
    			
    				var temp = -1;
	    			for (var i = 0; i < $scope.pravnaLica.length; i++) { 
					    if(angular.equals($scope.pravnaLica[i].id, response.data.id)){
					    	temp = i;
					    	break;
					    }
					}
					
    				if(!angular.equals(temp, -1)){
    					$scope.pravnaLica[i] = response.data;
    				}
    				
    				$scope.pravnoLice = {};
    				$scope.selektovanoNaseljenoMesto = {};
    		});
			
		}else if(angular.equals($scope.rezim, 2)){// & !angular.equals($scope.pravnoLice, {})){
			
			
			//if($scope.sakrijBrowse){
				$http.get('http://localhost:8080/filtrirajPravnaLicaZaNaseljenoMesto/'+$scope.pravnoLice.jmbg+'/'+$scope.pravnoLice.ime+'/'+$scope.pravnoLice.prezime+'/'+$scope.pravnoLice.adresa+'/'+$scope.pravnoLice.telefon+'/'+$scope.pravnoLice.email+'/'+$scope.pravnoLice.pib+'/'+$scope.pravnoLice.fax+'/'+$scope.pravnoLice.odobrio+'/'+$scope.selektovanoNaseljenoMesto.id).
		        then(function(response) {
		        	
		        	$scope.pravnaLica = response.data;
		        	
		        });
				return;
			//}
			
			//$http.get('http://localhost:8080/filtrirajPravnaLica/'+$scope.pravnoLice.jmbg+'/'+$scope.pravnoLice.ime+'/'+$scope.pravnoLice.prezime+'/'+$scope.pravnoLice.adresa+'/'+$scope.pravnoLice.telefon+'/'+$scope.pravnoLice.email+'/'+$scope.pravnoLice.pib+'/'+$scope.pravnoLice.fax+'/'+$scope.pravnoLice.odobrio).
	        //then(function(response) {
	        	
	        //	$scope.pravnaLica = response.data;
	        	
	        //});
			
			
		}
		
		
	};
	
	
	
	this.rollbackClick = function(){
		if(angular.equals($scope.rezim, 1) || angular.equals($scope.rezim, 2)){
			$scope.rezim = 0;
			$scope.pravnoLice = {};
			$scope.selektovanoNaseljenoMesto = {};
		}else{
			$scope.rezim = 0;
			
			$scope.pravnoLice = {};
			$scope.selektovanoNaseljenoMesto = {};
		}
	};
	
	
	
	this.deleteClick= function(){
		
		if(angular.equals($scope.pravnoLice, {})){
			
			return;
		}
		
		$http.delete('http://localhost:8080/obrisiPravnoLice/'+$scope.pravnoLice.id).
        then(function(response) {
        	
        	if(response.data.id == -1){
				toastr.error('Neuspesan unos!');
				return;
			}
        	
        	var temp = -1;
    		for (var i = 0; i < $scope.pravnaLica.length; i++) { 
    		    if(angular.equals($scope.pravnaLica[i].id, $scope.pravnoLice.id)){
    		    	temp = i;
    		    	break;
    		    }
    		}
    		
    		if(!angular.equals(temp, -1)){
    			$scope.pravnaLica.splice(temp, 1);
    		}
    		
    		$scope.pravnoLice = {};
    		$scope.selektovanoNaseljenoMesto = {};
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
		
		$scope.pravnoLice = angular.copy($scope.pravnaLica[0]);
		$scope.idSelektovanogPravnogLica = $scope.pravnaLica[0].id;
		$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnaLica[0].naseljenoMesto);
	};

	this.prevClick = function(){
		
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.pravnaLica.length; i++) { 
		    if(angular.equals($scope.pravnaLica[i].id, $scope.pravnoLice.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == 0 & temp!=-1){
			return;
		}
		
		$scope.pravnoLice = angular.copy($scope.pravnaLica[temp-1]);
		$scope.idSelektovanogPravnogLica = $scope.pravnaLica[temp-1].id;
		$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnaLica[temp-1].naseljenoMesto);
	};
	

	this.nextClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.pravnaLica.length; i++) { 
		    if(angular.equals($scope.pravnaLica[i].id, $scope.pravnoLice.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == $scope.pravnaLica.length-1 & temp!=-1){
			return;
		}
		
		$scope.pravnoLice = angular.copy($scope.pravnaLica[temp+1]);
		$scope.idSelektovanogPravnogLica = $scope.pravnaLica[temp+1].id;
		$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnaLica[temp+1].naseljenoMesto);
	};
	

	this.lastClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		$scope.pravnoLice = angular.copy($scope.pravnaLica[$scope.pravnaLica.length-1]);
		$scope.idSelektovanogPravnogLica = $scope.pravnaLica[$scope.pravnaLica.length-1].id;
		$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnaLica[$scope.pravnaLica.length-1].naseljenoMesto);
	};
	
	this.aktivirajRacun = function(pravnoLice){
		if(confirm("Da li ste sigurni da zelite da otvorite racun?")){
			var klijent = {};
			klijent.id = pravnoLice.id;
			klijent.jmbg = pravnoLice.jmbg;
			klijent.ime = pravnoLice.ime;
			klijent.prezime = pravnoLice.prezime;
			klijent.adresa = pravnoLice.adresa;
			klijent.telefon = pravnoLice.telefon;
			klijent.email = pravnoLice.email;
			klijent.naseljenoMesto = pravnoLice.naseljenoMesto;
			
			$http({
			    method: 'POST',
			    url: 'http://localhost:8080/noviRacun',
			    data: klijent
			}).
			then(function mySucces(response) {
				toastr.success("Racun uspesno aktiviran!");
			});
		}
	}
	
	this.setSelected = function(nm){
		if(angular.equals($scope.rezim, 0)){
			$scope.idSelektovanogPravnogLica = nm.id;
			$scope.pravnoLice = angular.copy(nm);
			$scope.selektovanoNaseljenoMesto = angular.copy(nm.naseljenoMesto);
		}
	};
	
	this.setSelectedPlace = function(nm){
		//if(angular.equals($scope.rezim, 0) || angular.equals($scope.rezim, 1)){
			
			$scope.selektovanoNaseljenoMesto = angular.copy(nm);
		//}
	};
	
	
	this.conf = function(){
		if(angular.equals($scope.rezim, 0)){
					
			if(angular.equals($scope.pravnoLice, {})){
				
				$scope.selektovanoNaseljenoMesto = {};
			}
			
			
			
		}else if(angular.equals($scope.rezim, 1)){
			
			
			
		}else if(angular.equals($scope.rezim, 2)){
			//$scope.selektovanoNaseljenoMesto = {};
		}
	}
	
	this.dismis = function(){
		 
		if(angular.equals($scope.rezim, 0)){
			
			if(!angular.equals($scope.pravnoLice, {})){
				
				$scope.selektovanoNaseljenoMesto = angular.copy($scope.pravnoLice.naseljenoMesto);
			}
			
		}
		
		$scope.selektovanoNaseljenoMesto = {};
		
	}
	
	this.nextFormClick = function(){
		
		$scope.$parent.$parent.opsti.tabClick7(7, $scope.pravnoLice);
		
	};
});

administrator.directive('ngConfirmClick', [
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

administrator.filter('stringRezima', function() {
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