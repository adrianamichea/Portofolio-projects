//promise pentru imaginea din background
function loadImage(url) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => {
      resolve(img);
    };
    img.onerror = () => {
      reject(new Error('Failed to load image'));
    };
    img.src = url;
  });
}

loadImage(
  'https://a.ltrbxd.com/resized/sm/upload/57/d5/ab/2r/creed-iii-1200-1200-675-675-crop-000000.jpg?v=f7ca1486ab'
)
  .then((img) => {
    const myImage = document.getElementById('background-image');
    myImage.src = img.src;
  })
  .catch((error) => {
    console.error(error);
  });
