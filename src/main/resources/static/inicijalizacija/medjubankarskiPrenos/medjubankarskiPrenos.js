administrator.controller('MedjubankarskiPrenosController', function($scope, $http, $compile,$log){
	
	$scope.medjubankarskiPrenos={};
	$scope.medjubankarskiPrenosi=[];
	$scope.banke=[];
	$scope.poruke=[];
	$scope.banks=[];
	$scope.medjubankarskiPrenosDTO={};
	var  State={
		VIEW_EDIT: 0,
		ADD : 1,
	    SEARCH : 2
	}
	
	$scope.state=State.VIEW_EDIT;
	
	
	this.searchClick=function(){
		$scope.state=State.SEARCH;
		
	}
	
	this.firstClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("First click");
			$scope.medjubankarskiPrenos=$scope.medjubankarskiPrenosi[0];
			//$scope.valutaId=selectedValuta.id;
		//	$scope.valuta=selectedValuta;
		
		
		}
	}
	
	this.refresh=function(){
		var path="/medjubankarskiPrenos";
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				
				$scope.medjubankarskiPrenosi=response.data;
			},function errorCallback(response){
				
			}
		);
		
		path="/messages";
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				$log.log("ucitavanje svih zabiljezenih poruka");
				$scope.poruke=response.data;
			},function errorCallback(response){
				
			}
		);
		
		path='/allBanks';
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				
				angular.forEach(response.data, function (element, index) {
			
					$scope.banks.push(element);
					
				});
			
				
			}, function errorCallback(response){
				$log.log("Error callback");
			}
		);
	}
	
	this.init=function(){
		$scope.state=State.VIEW_EDIT;
		this.refresh();
	}
	this.searchClick=function(){
		$scope.state=State.SEARCH;

	}
	this.firstClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("First click");
			$scope.medjubankarskiPrenos=$scope.medjubankarskiPrenosi[0];
			//$scope.valutaId=selectedValuta.id;
		//	$scope.valuta=selectedValuta;
		
		
		}
	}
	
	/*
		Selektovanje prve ispred stavke u odnosu na tenutno selektovanu.
	*/
	this.prevClick=function(){
		if($scope.state===State.VIEW_EDIT){
			var temp=findIndexOfValuta($scope.medjubankarskiPrenos.id);
			$log.log("Index selektovane stavke "+ temp);

			if(temp!=-1 && temp!=0){
				$scope.medjubankarskiPrenos=$scope.medjubankarskiPrenosi[temp-1];
			
			}
		}
	}
	/*
		Selektovanje prve sledece stavke u tabeli. Ako je posljednja stavka selektovana 
		nista se ne desava.
	*/
	
	findIndexOfValuta=function(id){
		var temp=-1;
		for (var i = 0; i < $scope.medjubankarskiPrenosi.length; i++) { 
				if(angular.equals($scope.medjubankarskiPrenosi[i].id, id)){
					temp = i;
					return temp;
				}
			}
		return temp;
	}
	this.lastClick=function(){
		if($scope.state===State.VIEW_EDIT){
		
			$scope.medjubankarskiPrenos=$scope.medjubankarskiPrenosi[$scope.medjubankarskiPrenosi.length-1];
			
			
		}
	}
	this.nextClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("Next valuta");
		//	$log.log("Oznaka djelatnost "+ $scope.message+" "+ $scope.activity.name+" id "+$scope.activity.id);
			
			var temp=findIndexOfValuta($scope.medjubankarskiPrenos.id);
			$log.log("Index selektovane stavke "+ temp);
			if(temp!=-1 && temp!=$scope.medjubankarskiPrenosi.length){
				$scope.medjubankarskiPrenos=$scope.medjubankarskiPrenosi[temp+1];
			
			}
		}
	}
	this.commitClick=function(){
		
		
		$log.log("Stanje "+ $scope.state);
		/*if($scope.medjubankarskiPrenosDTO.banka==undefined){
			$log.log("Undefined su");
			$scope.medjubankarskiPrenosDTO.banka={};
		}
		//$scope.medjubankarskiPrenosDTO.banka={};
		if($scope.medjubankarskiPrenosDTO.poruka==undefined){
			$log.log("Undefined su");
			$scope.medjubankarskiPrenosDTO.poruka={};
		} */
		//$scope.medjubankarskiPrenosDTO.poruka={};
	//	$log.log("Banka "+$scope.medjubankarskiPrenosDTO.banka.name);
	//	$log.log("Poruka "+$scope.medjubankarskiPrenosDTO.poruka.code);
	//	$log.log("Iznos1 "+$scope.medjubankarskiPrenosDTO.iznos1);
		 if($scope.state==State.SEARCH){
			var path="/medjubankarskiPrenosi";
			$http({
				method: 'POST',
				url: path,
				data: $scope.medjubankarskiPrenosDTO
			}).then(
				function successCallback(response){
					$log.log("Successs");
					$scope.medjubankarskiPrenosi=response.data;
					$scope.medjubankarskiPrenosDTO={};
					
				}, 
				function errorCallback(response){
					$log.log("ERROR");
				}
			);
		} 
		else {
			$log.log("Nije obradjeno");
		}
	}
	
	var check=function(){
		/*	$log.log("Vrijednosti koje se provjeravaju "+ $scope.message.code );
			if(angular.equals($scope.message, {})){
				toastr.error('Ne mozete ostaviti polja prazna');
				return false;
			}else if(angular.isUndefined($scope.message.code)){
				
				toastr.error('Oznaka  mora sadrzati tacno 5 karaktera');
				return false;
			}else if(!angular.equals($scope.message.code.trim().length, 5)){
				toastr.error('Oznaka mora da sadrzi 5 karaktera!');
				return false;
			} */
		$log.log("TRUE");
		return true;
	} 
	
	this.rollbackClick=function(){
		$log.log("Ponisti stanje");
		$scope.state=State.VIEW_EDIT;
	}
	
	this.setSelected=function(medjubankarskiPrenos){
		
		
		$scope.medjubankarskiPrenos=medjubankarskiPrenos;
	}
	
	this.nextFormClick = function(){
		$log.log("XXXXXXX");
		$log.log("Next mexanizam");
		$scope.$parent.$parent.opsti.tabClickMedjubankarski11(11, $scope.medjubankarskiPrenos);
		
	};
	

});