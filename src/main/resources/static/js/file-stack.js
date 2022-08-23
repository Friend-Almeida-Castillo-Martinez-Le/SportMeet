function pickFile(){
    let client = filestack.init(FILESTACK_API);
    client.pick({
        //launching image picker and adding the restrictions of one file that is of an image file type
        accept: 'image/*',
        maxFiles: 1,
    })
        //Taking the results object in as 'result'
        .then(function (result) {
            //Putting the returned file URL result in a string, and printing it to the console
            console.log(JSON.stringify(result.filesUploaded[0].url));
        });

    window.onload = function() {
        let btn = document.getElementById("filePicker");
        btn.onclick = pickFile;
    }

}