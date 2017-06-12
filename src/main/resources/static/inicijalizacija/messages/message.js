administrator.controller('MessagesController', function($scope, $http, $compile,$log){
	$scope.message={};
	$scope.messages=[];
	$scope.messagesPomocni={};
	var  State={
		VIEW_EDIT: 0,
		ADD : 1,
	    SEARCH : 2
	}
	
	$scope.state=State.VIEW_EDIT;
	this.addClick=function(){
		$log.log("ADD");
		$scope.state=State.ADD;
		$scope.message={};
		$scope.messagePomocni={};
	}
	
	this.searchClick=function(){
		$scope.state=State.SEARCH;
		
	}
	
	
	
	
	this.init=function(){
		$scope.state=State.VIEW_EDIT;
		this.refresh();
	}
	this.searchClick=function(){
		$scope.state=State.SEARCH;
	//	saveState();
	}
	this.firstClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("First click");
			$scope.message=$scope.messages[0];
			$scope.messagePomocni=$scope.message;
			//$scope.valutaId=selectedValuta.id;
		//	$scope.valuta=selectedValuta;
		
		
		}
	}
	this.refresh=function(){
		var path="/messages";
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				$log.log("ucitavanje svih zabiljezenih poruka");
				$scope.messages=response.data;
			},function errorCallback(response){
				
			}
		);
	}
	/*
		Selektovanje prve ispred stavke u odnosu na tenutno selektovanu.
	*/
	this.prevClick=function(){
		if($scope.state===State.VIEW_EDIT){
			var temp=findIndexOfMessage($scope.message.id);
			$log.log("Index selektovane stavke "+ temp);

			if(temp!=-1 && temp!=0){
				$scope.message=$scope.messages[temp-1];
				$scope.messagePomocni=angular.copy($scope.message);
			
			}
		}
	}
	/*
		Selektovanje prve sledece stavke u tabeli. Ako je posljednja stavka selektovana 
		nista se ne desava.
	*/
	
	findIndexOfMessage=function(id){
		var temp=-1;
		for (var i = 0; i < $scope.messages.length; i++) { 
				if(angular.equals($scope.messages[i].id, id)){
					temp = i;
					return temp;
				}
			}
		return temp;
	}
	this.lastClick=function(){
		if($scope.state===State.VIEW_EDIT){
		
			$scope.message=$scope.messages[$scope.messages.length-1];
			$scope.messagePomocni=angular.copy($scope.message);
			
			
		}
	}
	this.nextClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("Next valuta");
		//	$log.log("Oznaka djelatnost "+ $scope.message+" "+ $scope.activity.name+" id "+$scope.activity.id);
			
			var temp=findIndexOfMessage($scope.message.id);
			$log.log("Index selektovane stavke "+ temp);
			$log.log("Id "+$scope.message.id)
			if(temp!=-1 && temp!=$scope.messages.length){
				$scope.message=$scope.messages[temp+1];
				$scope.messagePomocni=angular.copy($scope.message);
			
			}
		}
	}
	this.commitClick=function(){
		
		
		$log.log("Stanje "+ $scope.state);
		$log.log("Activity "+$scope.message.code);
		if($scope.state==State.ADD && check()){
			$log.log("Stanje dodavanja");
			var path="/message";
			$http({
				method: 'PUT',
				url: path,
				data: $scope.messagePomocni
			}).then(
			function successCallback(response){
				
				$scope.messages.push(response.data);
				$scope.message={};
				$scope.messagePomocni={};
				toastr.success('Uspijesno ste dodali poruku.');
			},
			function errorCallback(response){
				$log.log("Greska-error");
				if(response.status==400){
					toastr.error('Unijeli ste pogresan format poruke. Ispravan format je MTXXX pri cemu je X cifra.');
				}else if(response.status==500){
					toastr.error('Doslo je do interne greske na serveru pokusajte ponovo.');
				}
			}
			);
		}else if($scope.state==State.VIEW_EDIT && check() ){
			$log.log("stAanje izmjene")
			var path="/message";
			
			$http({
				method: 'POST',
				url: path,
				data: $scope.messagePomocni
			}).then(
			function successCallback(response){
				$log.log("Success");
				var index=findIndexOfMessage($scope.messagePomocni.id);
				$scope.messages[index]=response.data;
				//$scope.message=response.data;
				
			}, 
			function errorCallback(response){
				$log.log("Error");
			});
		}else if($scope.state==State.SEARCH){
			var path="/messageByCode/"+$scope.messagePomocni.code;
			$http({
				method: 'GET',
				url: path
			}).then(
				function successCallback(response){
					
					$scope.messages=response.data;
					$scope.message={};
					$scope.messagePomocni={};
					
				}, 
				function errorCallback(response){
					if(response.status==204){
						$log.log("204");
						toaster.info("Ne postoje poruke sa parametrima koje ste unijeli.");
					}
				}
			);
		} 
		else {
			$log.log("Nije obradjeno");
		}
	}
	
	var check=function(){
			$log.log("Vrijednosti koje se provjeravaju "+ $scope.message.code );
			if(angular.equals($scope.messagePomocni, {})){
				toastr.error('Ne mozete ostaviti polja prazna');
				return false;
			}else if(angular.isUndefined($scope.messagePomocni.code)){
				
				toastr.error('Oznaka  mora sadrzati tacno 5 karaktera');
				return false;
			}else if(!angular.equals($scope.messagePomocni.code.trim().length, 5)){
				toastr.error('Oznaka mora da sadrzi 5 karaktera!');
				return false;
			}
		$log.log("TRUE");
		return true;
	} 
	
	this.rollbackClick=function(){
		$log.log("Ponisti stanje");
		$scope.state=State.VIEW_EDIT;
	}
	
	this.setSelected=function(message){
		
		
		$scope.message=message;
		$scope.messagePomocni=angular.copy($scope.message);
		$log.log("ID "+ $scope.message.id);
	}
	
	
	this.deleteClick=function(){
		var path='/message/'+ $scope.message.id;
		$log.log("Path "+ path);
		$http({
			method: 'DELETE',
			url: path
		}).then(
			function successCallback(response){
				var index=findIndexOfMessage(response.data.id);
				$log.log("Index stavke koja se brise je "+ index);
				$scope.messages.splice(index,1);
				$scope.message={};
				
			}, 
			function errorCallback(response){
			}
		);
		
	}
});