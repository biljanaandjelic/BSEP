
certificateModule.controller('IzmenaCtrl', [ '$window', '$scope', '$http', '$compile', 
		function($window, $scope, $http, $compile) {

			$scope.userToChange = {}

			
			$scope.lozinka1 = "";
			
			$scope.poruka = "";	
			$scope.$watch('userToChange["newPassword"]', function (newValue, oldValue, scope) {
				
				var brojMin = new RegExp("^([A-Za-z0-9]{0,7})$");
				var brojMax = new RegExp("^([A-Za-z0-9]{26,})$");
				var samoVelika = new RegExp("^([A-Z]{8,25})$");
				var samoMala = new RegExp("^([a-z]{8,25})$");
				var samoCifre = new RegExp("^([0-9]{8,25})$");
				
				var samoMalaIVelika = new RegExp("^([a-zA-Z]{8,25})$");
				var samoMalaICifre = new RegExp("^([a-z0-9]{8,25})$");
				var samoVelikaICifre = new RegExp("^([0-9A-Z]{8,25})$");
				
				var uobicajeni = new RegExp("^[A-Z]([a-z]{6,23})[0-9]$");
				
				if (brojMin.test(newValue)) {
				    scope.poruka = "Lozinka se ne sme sastojati iz manje od 8 karaktera!";
				}else if(brojMax.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati iz vise od 25 karaktera!";
				}else if(samoVelika.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od velikih slova!";
				}else if(samoMala.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od malih slova!";
				}else if(samoCifre.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od cifara!";
				}else if(samoMalaIVelika.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od slova!";
				}else if(samoMalaICifre.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od malih slova i cifara!";
				}else if(samoVelikaICifre.test(newValue)){
					scope.poruka = "Lozinka se ne sme sastojati samo od velikih slova i cifara!";
				}else if(uobicajeni.test(newValue)){
					scope.poruka = "Vasa lozinka se uklapa u sablon cestih lozinki sto je cini ranjivom.";
				}else{
					scope.poruka = "Lozinka zadovoljava osnovne kriterijume bezbedne lozinke."
				}
			});
			
			this.logoff = function(){
				
				$http({
				    method: 'POST',
				    url: '/logoff',
				    data: $scope.userToChange
				}).
				then(function mySucces(response) {
					
						//$window.location.href="http://localhost:8080/authentification/login.html";
						
				});
			};
			
			this.submitClick = function(){
				
				if(angular.equals($scope.userToChange, {})){
					return;
				}
				
				var newValue = $scope.userToChange.newPassword;
				
				var brojMin = new RegExp("^([A-Za-z0-9]{0,7})$");
				var brojMax = new RegExp("^([A-Za-z0-9]{26,})$");
				var samoVelika = new RegExp("^([A-Z]{8,25})$");
				var samoMala = new RegExp("^([a-z]{8,25})$");
				var samoCifre = new RegExp("^([0-9]{8,25})$");
				
				var samoMalaIVelika = new RegExp("^([a-zA-Z]{8,25})$");
				var samoMalaICifre = new RegExp("^([a-z0-9]{8,25})$");
				var samoVelikaICifre = new RegExp("^([0-9A-Z]{8,25})$");
				
				var uobicajeni = new RegExp("^[A-Z]([a-z]{6,23})[0-9]$");
				
				if (brojMin.test(newValue)) {
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(brojMax.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoVelika.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoMala.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoCifre.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoMalaIVelika.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoMalaICifre.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}else if(samoVelikaICifre.test(newValue)){
					toastr.error('Neispravan format lozinke!');
					return;
				}
				
				
				$http({
				    method: 'POST',
				    url: '/passwordChange',
				    data: $scope.userToChange
				}).
				then(function mySucces(response) {
					
					
					if(response.data.id == -1){
						toastr.error('Ne postoji korisnik sa takvim korisnickim imenom i lozinkom!');
						return;
					}else if(response.data.id == -2){
						toastr.error('Uneti podaci se ne poklapaju sa podacima ulogovanog korisnika');
						return;
					}else if(response.data.id == -5){
						
						$window.location.href=response.data.username;
						
						return;
					}else{
						toastr.success('Uspesno logovanje!');
						return;
					}
				
					
				});
			};
			

		} ]);

certificateModule.directive("passwordVerify", function() {
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