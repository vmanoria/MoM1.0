function openPage(evt, pageName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    document.getElementById(pageName).style.display = "block";
    evt.currentTarget.className += " active";
}

function showSection(hideSectionId, showSectionId){
    document.getElementById(hideSectionId).style.display = "none";
    document.getElementById(showSectionId).style.display = "block";
}
    
function searchFunction(searchButtonId, tableId) {
  // Declare variables
  var input, filter, table, tr, td, i;
  input = document.getElementById(searchButtonId);
  filter = input.value.toUpperCase();
  table = document.getElementById(tableId);
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}

function getSelectedList (chkboxName){
    var allItemsList = document.getElementsByName(chkboxName);    
    var aLength = allItemsList.length;
    var selectedList = [];
    if(aLength > 0) {
        for(var i=0;i<aLength;i++){
            if(allItemsList[i].checked) {
                selectedList.push(allItemsList[i].value);
            }
        }
    }

    return selectedList;
}

function toggleDeleteBtn(checkboxName, deleteBtnId){
    var x = getSelectedList(checkboxName);   
    var delBtn= document.getElementById(deleteBtnId);
    if(x.length > 0){            
        delBtn.className = "";
    } else {
        delBtn.className = "disabled";
    }
}

function validatePassword(){
    var password = document.getElementById("psw");
    var confirm_password = document.getElementById("confirm_psw");

    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Passwords don't match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

function getParameter(name){
	var paramValue = null;
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
    	paramValue = decodeURIComponent(name[1]);
    return paramValue;
}

function openDialog(modalName){
    // Get the modal
    var modal = document.getElementById(modalName);
    modal.style.display = "block";
}

function closeDialog(modalName){
    // When the user clicks on <span> (x), close the modal
    var modal = document.getElementById(modalName);
    modal.style.display = "none";
}
function refreshPage(){
    window.location.reload(true);
}