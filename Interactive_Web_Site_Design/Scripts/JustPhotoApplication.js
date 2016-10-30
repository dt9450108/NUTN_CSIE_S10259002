//lastModified: 1420817434000
//lastModifiedDate: Fri Jan 09 2015 23:30:34 GMT+0800 (台北標準時間)
//name: "2015_01_09_23_30_34.png"
//size: 19797
//type: "image/png"
//webkitRelativePath: ""

/*
*       UploadPicturePageCheck function
*       no parameter
*       just check for UploadPicture.aspx page that the file limit
*       file size less than 5mb
*       file type allow image/png image/jpg image/jpeg
*/
function UploadPicturePageCheck() {
    var $fileupload = $('#UploadPageFileUpload');

    var gotFile;

    // browser not support input type file
    if (!$fileupload[0].files) {
        alert('瀏覽器不支援本網站上傳方式');
    } else if (!$fileupload[0].files[0]) {
        // if select file
        $('#UploadPageFileUpload')[0].value = '';
        alert('尚未選擇檔案');
    } else {
        // get select file
        gotFile = $fileupload[0].files[0];

        // first check file extension
        var fileType = gotFile.type;
        if (fileType != 'image/png' && fileType != 'image/jpeg' && fileType != 'image/jpg') {
            $('#UploadPageFileUpload')[0].value = '';
            alert('檔案格式錯誤，需為jpeg、jpg、png檔案格式');
            return;
        }

        // second check file size
        var fileSize = gotFile.size;
        if (fileSize > 5242880) {
            $('#UploadPageFileUpload')[0].value = '';
            alert('檔案大小超過上傳限制，請選擇5 MB以下之檔案');
            return;
        }

        var imgFileReader = new FileReader();
        var thisImg = new Image();

        imgFileReader.readAsDataURL(gotFile);

        imgFileReader.onload = function (_file) {
            thisImg.src = _file.target.result;

            thisImg.onload = function () {
                if (this.width > 640) {
                    var picWidth = this.width;
                    var picHeight = this.height;
                    // rate for resize
                    var sizeRate = picHeight / picWidth;
                    // new height for pic
                    var newHeight = 640 * sizeRate;
                    // resize pic
                    this.width = 640;
                    this.height = newHeight;
                }
                $('#UploadPageTempPreview').attr('src', this.src);
                $('#UploadPageTempPreview').attr('width', this.width);
                $('#UploadPageTempPreview').attr('height', this.height);
                $('#UploadPageTempPreview').attr('alt', '已有圖片可預覽');
            };
        };

        // change display
        $('#UploadPageTempPreview').removeAttr('hidden');
        $('#BtnUploadPicPageReset').removeAttr('hidden');
        $fileupload.attr('hidden', 'hidden');
    }
}

function UploadPicturePageReset() {
    $('#UploadPageFileUpload')[0].value = '';

    // change display
    $('#UploadPageTempPreview').attr('hidden', 'hidden');
    $('#BtnUploadPicPageReset').attr('hidden', 'hidden');
    $('#UploadPageFileUpload').removeAttr('hidden');
}

/*
*       UserHeadPicPageCheck function
*       no parameter
*       just check for UserHeadPic.aspx page that the file limit
*       file size less than 1mb
*       file type allow image/png image/jpg image/jpeg
*/
function UserHeadPicPageCheck() {
    var $fileupload = $('#UserHeadPicPageFileUpload');

    var gotFile;

    // browser not support input type file
    if (!$fileupload[0].files) {
        alert('瀏覽器不支援本網站上傳方式');
    } else if (!$fileupload[0].files[0]) {
        $fileupload[0].value = '';
        // if select file
        //alert('尚未選擇檔案');
    } else {
        // get select file
        gotFile = $fileupload[0].files[0];

        // first check file extension
        var fileType = gotFile.type;
        if (fileType != 'image/png' && fileType != 'image/jpeg' && fileType != 'image/jpg') {
            $('#UserHeadPicPageFileUpload')[0].value = '';
            alert('檔案格式錯誤，需為jpeg、jpg、png檔案格式');
            return;
        }

        // second check file size
        var fileSize = gotFile.size;
        if (fileSize > 1048576) {
            $('#UserHeadPicPageFileUpload')[0].value = '';
            alert('檔案大小超過上傳限制，請選擇1 MB以下之檔案');
            return;
        }

        var imgFileReader = new FileReader();
        var thisImg = new Image();

        imgFileReader.readAsDataURL(gotFile);

        imgFileReader.onload = function (_file) {
            thisImg.src = _file.target.result;

            thisImg.onload = function () {
                if (this.width > 120) {
                    var picWidth = this.width;
                    var picHeight = this.height;
                    // rate for resize
                    var sizeRate = picHeight / picWidth;
                    // new height for pic
                    var newHeight = 120 * sizeRate;
                    // resize pic
                    this.width = 120;
                    this.height = newHeight;
                }
                $('#UserHeadPicPageTempPreview').attr('src', this.src);
                $('#UserHeadPicPageTempPreview').attr('width', this.width);
                $('#UserHeadPicPageTempPreview').attr('height', this.height);
                $('#UserHeadPicPageTempPreview').attr('alt', '已有頭像可預覽');
            };
        };
    }
}

function UserHeadPicPageReset() {
    $('#UserHeadPicPageFileUpload')[0].value = '';

    // change display
    $('#UserHeadPicPageTempPreview').attr('src', '/img/guset_448_448.png');
}

// function to delete div from client
function deletePhotoDivById(divTagId) {
    $('#' + divTagId).remove();
}