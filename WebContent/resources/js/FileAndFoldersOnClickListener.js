 var nameApp = angular.module('nameApp', []);

  nameApp.controller('NameCtrl', function($scope, $http, $sce){
	  
	  $scope.preparePath = function(path) {
			var preparedPath = path.split('\\').join('\\\\');
			return preparedPath;
	  };
	  
	  $scope.createFilesTable = function(jsonFiles, folderID) {
			var filesTable = "";
			for (var i = 0; i < jsonFiles.length; i++) {
				var file = jsonFiles[i];
				if(file.folderID==folderID){
					var name = file.name;
					var fileID = file.fileID;
					var path = file.path;
					filesTable = filesTable 
							+"<nobr><table width=\"40%\" class=\"block\"><tr><td>"
							+ " ↳ "
							+"<button "
							+ "ng-click=\"fileBtnClick('"
							+ name+ "&%&"
							+ folderID+ "&%&"
							+ fileID+ "&%&"
							+ $scope.preparePath(path)
							+ "')\">"
							+ name
							+"</button>"
							+ "</td></tr></table></nobr>";
				};
			};
			return filesTable;
	  };
	  
	  
	  $scope.createFoldersTable = function(jsonFolders, parentID, jsonFiles) {
			var foldersAndFilesTable = "";
			for (var i = 0; i < jsonFolders.length; i++) {
				var folder = jsonFolders[i];
				if(folder.parentID==parentID){
					foldersAndFilesTable = foldersAndFilesTable
							+"<nobr><table width=\"40%\" class=\"block\"><tr><td>"
							+ " ↳ "
							+"<button "
							+ "ng-click=\"folderBtnClick('"
							+ folder.name+ "&%&"
							+ parentID+ "&%&"
							+ folder.folderID+ "&%&"
							+ $scope.preparePath(folder.path)
							+ "')\">"
							+ "<img src=\"resources/images/folder30.png\">"
							+ folder.name
							+ "</button>"
							+ $scope.createFilesTable(jsonFiles, folder.folderID)
							+ $scope.createFoldersTable(jsonFolders,folder.folderID,jsonFiles)
							+ "</td></tr></table></nobr>";
				};
			};
			return foldersAndFilesTable;
	  };
	  
	  $scope.getRootID = function(jsonFolders){
		  var rootID;
		  for (var i = 0; i < jsonFolders.length; i++) {
			    var folder = jsonFolders[i];
			    if(folder.parentID == 0){
			    	rootID = folder.folderID;
			    	break;
			    }
		  }
		  return rootID;
	  };
	  
	  $scope.getTree = function(){
	    	$http({
			  method: 'GET',
			  url: 'FoldersAndFilesTreeJSON' 
			}).then(function successCallback(response) {
				
				var jsonFiles = JSON.parse(response.data.files.string);
				
				var jsonFolders = JSON.parse(response.data.folders.string);
				$scope.folderList ='';
				for (var i = 0; i < jsonFolders.length; i++) {
				    var folder = jsonFolders[i];
				    var path = folder.path;
				    $scope.folderList =  $scope.folderList +"<option value="+path+">"+path+"</option>";
				}
				var rootID = $scope.getRootID(jsonFolders);
				
				$scope.tree = $scope.createFilesTable(jsonFiles, rootID) + $scope.createFoldersTable(jsonFolders, rootID, jsonFiles);
				
			}, function errorCallback(response) {
				console.log("Error");
				console.log(response);
			});
	    }();
	  
          $scope.rootBtnClick = function(email){
        	  $scope.rootControlsText =  springMessages['folder'] +' '+ email+" <br><br>"
				+ springMessages['file.upload']
				+ "<form action=\"UploadFile\" method=\"post\" enctype=\"multipart/form-data\">"
				+ "<input type=\"file\" name=\"file\" required />"
				+ "<input type=\"hidden\" name=\"parentPath\" value=\""+email+"\">"
				+ '<input type="submit" value="'+springMessages['file.upload.button']+'">'
		        + "</form>"
				
		        + "<br><br>"
		        
		        + springMessages['folder.create']
		        + "<form action=\"CreateFolder\" method=\"post\">"
		        + '<input name="folderName" placeholder="'+springMessages['folder.create.name']+'" type="text" required>'
		        + "<input type=\"hidden\" name=\"parentPath\" value=\""+email+"\">"
		        + '<input type="submit" value="'+springMessages['folder.create.button']+'">'
		        + "</form>";
        	  
				$scope.controlsText = $sce.trustAsHtml($scope.rootControlsText);
            };
            
            $scope.folderBtnClick = function(data){
            	var path = data.split('&%&')[3];
          	  $scope.folderControlsText =   springMessages['folder']+' '+path+" <br><br>"
				+ springMessages['file.upload']
				+ "<form action=\"UploadFile\" method=\"post\" enctype=\"multipart/form-data\">"
				+ "<input type=\"file\" name=\"file\" size=\"60\" required />"
				+ "<input type=\"hidden\" name=\"parentPath\" value=\""+path+"\">"
				+ '<input type="submit" value="'+springMessages['file.upload.button']+'">'
		        + "</form>"
				
		        + "<br><br>"
		        
		        + springMessages['folder.create']
		        + "<form action=\"CreateFolder\" method=\"post\">"
		        + '<input name="folderName" placeholder="'+springMessages['folder.create.name']+'" type=\"text\" required>'
		        + "<input type=\"hidden\" name=\"parentPath\" value=\""+path+"\">"
		        + '<input type=\"submit\" value=\"'+springMessages['folder.create.button']+'">'
		        + "</form>"
		
		        + "<br><br>"
		        
		        + springMessages['folder.delete'] 
				+ "<form method=\"post\"><button value=\""+data+"\" "
				+ "name=\"folderDeleteData\" formaction=\"DeleteFolderOrFile\">"
				+ springMessages['folder.delete.button']
				+ "</button></form>"
				
				+ "<br><br>"
		
				+ springMessages['folder.rename']
		        + "<form action=\"RenameFolderOrFile\" method=\"post\">"
		        + '<input name="newName" placeholder="'+springMessages['folder.rename.name']+'" type="text" required>'
		        + "<input type=\"hidden\" name=\"folderRenameData\" value=\""+data+"\">"
		        + '<input type="submit" value="'+springMessages['folder.rename.button']+'">'
				+ "</button></form>"
		        
				+ "<br><br>"

				+  springMessages['folder.copy']
		        + "<form action=\"CopyFolderOrFile\" method=\"post\">"
		        + '<input name="newName" placeholder="'+springMessages['folder.copy.name']+'" type="text" required>'
		        + "<input type=\"hidden\" name=\"folderCopyData\" value=\""+data+"\">"
		        + '<input type="submit" value="'+springMessages['folder.copy.button']+'">'
				+ "</button></form>"
		        
				+ "<br><br>"
		        
				+ springMessages['folder.move']
				+ "<form action=\"MoveFolderOrFile\" method=\"post\">"
				+ "<select name=\"parentPath\" size=\"1\" required>"
				+ '<option disabled>'+ springMessages['folder.move.name'] +'</option>'
				+ $scope.folderList
				+ "</select>"
				+ "<input type=\"hidden\" name=\"folderMoveData\" value=\""+data+"\">"
				+ '<input type="submit" value="'+ springMessages['folder.move.button'] +'">'
				+ "</form>";
  		        
  				$scope.controlsText = $sce.trustAsHtml($scope.folderControlsText);
             };
             
             $scope.fileBtnClick = function(data){
             	var arr = data.split('&%&');
             	var name = arr[0];
             	var parent_id = arr[1];
             	var id = arr[2];
             	var path = arr[3];
           	  $scope.fileControlsText =   springMessages['file']+' '+path+" <br><br>"
				+ springMessages['file.delete'] +' '+springMessages['file']
				+ "<form method=\"post\"><button value=\""+data+"\" "
				+ "name=\"fileDeleteData\" formaction=\"DeleteFolderOrFile\">"
				+ springMessages['file.delete'] 
				+ "</button></form>"
				
		        + "<br><br>"
		        
				+ springMessages['file.rename'] +' '+springMessages['file']
		        + "<form action=\"RenameFolderOrFile\" method=\"post\">"
		        + '<input name=\"newName\" placeholder=\"'+springMessages['folder.rename.name']+'" type=\"text\" required>'
		        + "<input type=\"hidden\" name=\"fileRenameData\" value=\""+data+"\">"
		        + '<input type=\"submit\" value="'+springMessages['file.rename']+'">'
				+ "</button></form>"
		        
		        + "<br><br>"
        
		        +  springMessages['file.move'] +' ' + springMessages['file']
		        + "<form action=\"MoveFolderOrFile\" method=\"post\">"
		        + "<select name=\"parentPath\" size=\"1\" required>"
				+ "<option disabled>"+springMessages['folder.move.name']+"</option>"
				+ $scope.folderList
				+ "</select>"
		        + "<input type=\"hidden\" name=\"fileMoveData\" value=\""+data+"\">"
		        + '<input type=\"submit\" value='+springMessages['file.move']+'>'
		        + "</form>"
		        
		        + "<br><br>"

		        + springMessages['file.copy']+ " " + springMessages['file']
		        + "<form action=\"CopyFolderOrFile\" method=\"post\">"
		        + '<input name=\"newName\" placeholder="'+springMessages['folder.copy.name']+'" type=\"text\" required>'
		        + "<input type=\"hidden\" name=\"fileCopyData\" value=\""+data+"\">"
		        + '<input type="submit" value="'+springMessages['file.copy']+'">'
				+ "</button></form>"
           	  
           	    + "<br><br>"
           	    
		        + springMessages['download']
		        + "<form action=\"DownloadFile\" method=\"post\">"
		        + "<input type=\"hidden\" name=\"filepath\" value=\""+path+"\">"
		        + '<input type="submit" value="'+springMessages['download']+'">'
				+ "</button></form>";
   		        
   				$scope.controlsText = $sce.trustAsHtml($scope.fileControlsText);
              };
  });
  
  
  nameApp.directive('dir', function($compile, $parse) {
	    return {
	        restrict: 'E',
	        link: function(scope, element, attr) {
	          scope.$watch(attr.content, function() {
	            element.html($parse(attr.content)(scope));
	            $compile(element.contents())(scope);
	          }, true);
	        }
	      }
	    })