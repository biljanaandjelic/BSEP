administrator.controller('RukovanjeNaseljenimMestima', function($scope, $http, $compile){
	
	$scope.rezim = 0;
	//0 za pregled, 1 za dodavanje, 2 za pretragu
	
	$scope.naseljenoMesto = {};
	$scope.naseljenaMesta = {};
	$scope.sveDrzave = {};

	
	$scope.sakrijBrowse = false;
	
	$scope.$on('filterPoDrzavi', function (event, id) {
	    console.log(id); // Index drzave
	    
	    $http.get('http://localhost:8080/nadjiNaseljenaMesta/'+id).
        then(function(response) {
        	$scope.naseljenaMesta = response.data;
        	$scope.selektovanaDrzava = angular.copy($scope.naseljenaMesta[0].drzava);
        	$scope.sakrijBrowse = true;
        });
	    
	  });
	
	$scope.idSelektovanogNaseljenogMesta = null;
	
	$scope.selektovanaDrzava = {};
	
	$scope.init = function(){
		
		$http.get('http://localhost:8080/sveDrzave').
        then(function(response) {
        	$scope.sveDrzave = response.data;
        	
        });
		
		$http.get('http://localhost:8080/svaNaseljenaMesta').
        then(function(response) {
        	$scope.naseljenaMesta = response.data;
        	
        });
		
		
	};
	
	
	this.refresh = function(){
		
		$scope.sakrijBrowse = false;
		
		$http.get('http://localhost:8080/svaNaseljenaMesta').
        then(function(response) {
        	$scope.naseljenaMesta = response.data;
        	
        });
	}
	
	this.addClick = function(){
		
		$scope.rezim =1;
		$scope.naseljenoMesto = {};
		$scope.selektovanaDrzava = {};
	};
	
	this.searchClick = function(){
		$scope.rezim =2;
		$scope.naseljenoMesto = {};
		if(!sakrijBrowse){
			$scope.selektovanaDrzava = {};
		}
	};
	
	this.commitClick = function(){
		
	
		if(angular.equals($scope.rezim, 1)){
			
			$scope.rezim = 1;
			
			if(angular.equals($scope.naseljenoMesto, {})){
				
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.oznaka)){
				toastr.error('Oznaka mora da ima tacno 2 karaktera!');
				return;
			}else if(!angular.equals($scope.naseljenoMesto.oznaka.trim().length, 2)){
				toastr.error('Oznaka mora da ima tacno 2 karaktera!');
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.naziv)){
				toastr.error('Naziv mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.postanskiBroj)){
				toastr.error('Postanski broj mora biti zadat!');
				return;
			}else if(angular.equals($scope.selektovanaDrzava, {})){
				toastr.error('Drzava mora biti zadata!');
				return;
			}
			
			$scope.naseljenoMesto.drzava = angular.copy($scope.selektovanaDrzava);
			
			$http({
    		    method: 'POST',
    		    url: 'http://localhost:8080/novoNaseljenoMesto',
    		    data: $scope.naseljenoMesto
    		}).
    		then(function mySucces(response) {
    				
    				if(response.data.id == -1){
    					toastr.error('Neuspesan unos!');
    					return;
    				}
    			
    				$scope.naseljenaMesta.push(response.data);
    				$scope.naseljenoMesto = {};
    				$scope.selektovanaDrzava = {};
    		});
      
		}else if(angular.equals($scope.rezim, 0) & !angular.equals($scope.naseljenoMesto, {}) & !angular.equals($scope.idSelektovanogNaseljenogMesta, null)){
			
			$scope.rezim = 0;
			

			if(angular.equals($scope.naseljenoMesto, {})){
				
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.oznaka)){
				toastr.error('Oznaka mora da ima tacno 2 karaktera!');
				return;
			}else if(!angular.equals($scope.naseljenoMesto.oznaka.trim().length, 2)){
				toastr.error('Oznaka mora da ima tacno 2 karaktera!');
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.naziv)){
				toastr.error('Naziv mora biti zadat!');
				return;
			}else if(angular.isUndefined($scope.naseljenoMesto.postanskiBroj)){
				toastr.error('Postanski broj mora biti zadat!');
				return;
			}else if(angular.equals(angular.equals($scope.selektovanaDrzava, {}))){
				toastr.error('Drzava mora biti zadata!');
				return;
			}
			
			$scope.naseljenoMesto.drzava = angular.copy($scope.selektovanaDrzava);
			
			$http({
    		    method: 'POST',
    		    url: 'http://localhost:8080/azurirajNaseljenoMesto',
    		    data: $scope.naseljenoMesto
    		}).
    		then(function mySucces(response) {
    			
	    			if(response.data.id == -1){
						toastr.error('Neuspesan unos!');
						return;
					}
    			
    				var temp = -1;
	    			for (var i = 0; i < $scope.naseljenaMesta.length; i++) { 
					    if(angular.equals($scope.naseljenaMesta[i].id, response.data.id)){
					    	temp = i;
					    	break;
					    }
					}
					
    				if(!angular.equals(temp, -1)){
    					$scope.naseljenaMesta[i] = response.data;
    				}
    				
    				$scope.naseljenoMesto = {};
    				$scope.selektovanaDrzava = {};
    		});
			
		}else if(angular.equals($scope.rezim, 2) & !angular.equals($scope.naseljenoMesto, {})){
			
			
			if($scope.sakrijBrowse){
				$http.get('http://localhost:8080/filtrirajNaseljenaMestaZaDrzavu/'+$scope.naseljenoMesto.oznaka+'/'+$scope.naseljenoMesto.naziv+'/'+$scope.naseljenoMesto.postanskiBroj+'/'+$scope.selektovanaDrzava.id).
		        then(function(response) {
		        	
		        	$scope.naseljenaMesta = response.data;
		        	
		        });
				return;
			}
			
			$http.get('http://localhost:8080/filtrirajNaseljenaMesta/'+$scope.naseljenoMesto.oznaka+'/'+$scope.naseljenoMesto.naziv+'/'+$scope.naseljenoMesto.postanskiBroj).
	        then(function(response) {
	        	
	        	$scope.naseljenaMesta = response.data;
	        	
	        });
			
			
		}
		
		
	};
	
	
	
	this.rollbackClick = function(){
		if(angular.equals($scope.rezim, 1) || angular.equals($scope.rezim, 2)){
			$scope.rezim = 0;
			$scope.naseljenoMesto = {};
			$scope.selektovanaDrzava = {};
		}else{
			$scope.rezim = 0;
			
			$scope.naseljenoMesto = {};
			$scope.selektovanaDrzava = {};
		}
	};
	
	
	
	this.deleteClick= function(){
		
		if(angular.equals($scope.naseljenoMesto, {})){
			
			return;
		}
		
		$http.delete('http://localhost:8080/obrisiNaseljenoMesto/'+$scope.naseljenoMesto.id).
        then(function(response) {
        	
        	if(response.data.id == -1){
				toastr.error('Neuspesan unos!');
				return;
			}
        	
        	var temp = -1;
    		for (var i = 0; i < $scope.naseljenaMesta.length; i++) { 
    		    if(angular.equals($scope.naseljenaMesta[i].id, $scope.naseljenoMesto.id)){
    		    	temp = i;
    		    	break;
    		    }
    		}
    		
    		if(!angular.equals(temp, -1)){
    			$scope.naseljenaMesta.splice(temp, 1);
    		}
    		
    		$scope.naseljenoMesto = {};
    		$scope.selektovanaDrzava = {};
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
		
		$scope.naseljenoMesto = angular.copy($scope.naseljenaMesta[0]);
		$scope.idSelektovanogNaseljenogMesta = $scope.naseljenaMesta[0].id;
		$scope.selektovanaDrzava = angular.copy($scope.naseljenaMesta[0].drzava);
	};

	this.prevClick = function(){
		
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.naseljenaMesta.length; i++) { 
		    if(angular.equals($scope.naseljenaMesta[i].id, $scope.naseljenoMesto.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == 0 & temp!=-1){
			return;
		}
		
		$scope.naseljenoMesto = angular.copy($scope.naseljenaMesta[temp-1]);
		$scope.idSelektovanogNaseljenogMesta = $scope.naseljenaMesta[temp-1].id;
		$scope.selektovanaDrzava = angular.copy($scope.naseljenaMesta[temp-1].drzava);
	};
	

	this.nextClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		
		var temp = -1;
		for (var i = 0; i < $scope.naseljenaMesta.length; i++) { 
		    if(angular.equals($scope.naseljenaMesta[i].id, $scope.naseljenoMesto.id)){
		    	temp = i;
		    	break;
		    }
		}
		
		if(temp == $scope.naseljenaMesta.length-1 & temp!=-1){
			return;
		}
		
		$scope.naseljenoMesto = angular.copy($scope.naseljenaMesta[temp+1]);
		$scope.idSelektovanogNaseljenogMesta = $scope.naseljenaMesta[temp+1].id;
		$scope.selektovanaDrzava = angular.copy($scope.naseljenaMesta[temp+1].drzava);
	};
	

	this.lastClick = function(){
		if(!$scope.nultoStanje()){
			return;
		}
		$scope.naseljenoMesto = angular.copy($scope.naseljenaMesta[$scope.naseljenaMesta.length-1]);
		$scope.idSelektovanogNaseljenogMesta = $scope.naseljenaMesta[$scope.naseljenaMesta.length-1].id;
		$scope.selektovanaDrzava = angular.copy($scope.naseljenaMesta[$scope.naseljenaMesta.length-1].drzava);
	};
	
	
	this.setSelected = function(d){
		if(angular.equals($scope.rezim, 0)){
			$scope.idSelektovanogNaseljenogMesta = d.id;
			$scope.naseljenoMesto = angular.copy(d);
			$scope.selektovanaDrzava = angular.copy(d.drzava);
		}
	};
	
	this.setSelectedState = function(d){
		if(angular.equals($scope.rezim, 0) || angular.equals($scope.rezim, 1)){
			
			$scope.selektovanaDrzava = angular.copy(d);
		}
	};
	
	
	this.conf = function(){
		if(angular.equals($scope.rezim, 0)){
					
			if(angular.equals($scope.naseljenoMesto, {})){
				
				$scope.selektovanaDrzava = {};
			}
			
			
			
		}else if(angular.equals($scope.rezim, 1)){
			
			
			
		}else if(angular.equals($scope.rezim, 2)){
			$scope.selektovanaDrzava = {};
		}
	}
	
	this.dismis = function(){
		 
		if(angular.equals($scope.rezim, 0)){
			
			if(!angular.equals($scope.naseljenoMesto, {})){
				
				$scope.selektovanaDrzava = angular.copy($scope.naseljenoMesto.drzava);
			}
			
		}
		
		$scope.selektovanaDrzava = {};
		
	}
	
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