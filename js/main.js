let jsonData;
let back_button = document.getElementsByClassName('back')[0];

async function fetchData() {
    try {
        const response = await fetch('./trasnform/data/data.json');
        jsonData = await response.json();
    } catch (error) {
        console.error('Ошибка загрузки данных JSON:', error);
    }

    const {data: country, obls} = jsonData;

    let clickCount = 0;
    let hoverElement = document.querySelectorAll(".bel_map g");
    let title = document.getElementById("title");
    let box = document.getElementsByClassName('box')[0];
    let values = document.getElementsByClassName('value');
    let numbers = document.getElementsByClassName('number');
    let rb = document.getElementsByClassName('rb')[0];


    function showSettlementInfo (currentMainObj, blocks, data) {
        let checky
        back_button.style.display =  currentMainObj === country ? 'none' : 'block'


        for (let i = 0; i < numbers.length; i++) {
            numbers[i].textContent = currentMainObj['n' + (i + 1)]
        }

        for (let item of blocks) {
            item.addEventListener("mouseover", function () {
                box.style.display = 'block'
                if(currentMainObj === country){
                    item.id === '6' ? title.textContent = 'город ' + data[item.id].name : title.textContent = data[item.id].name + ' область'
                } else {
                    title.textContent = data[item.id].name + ' район'
                }
                for (let i = 0; i < values.length; i++) {
                    values[i].textContent = data[item.id]['n' + (i + 1)]
                }
            });

            item.addEventListener("mouseout", function () {
                box.style.display = 'none'
            });
            currentMainObj.id === 'r1' && item.addEventListener("click", function (event) {
                if (item.contains(event.target) && checky === item) {
                    clickCount++;
                    if (clickCount === 2) {
                        item.parentNode.classList.remove("bel_map");
                        document.querySelectorAll(`.map svg`)[+item.id + 1].classList.add("bel_map")
                        let newHoverElement = document.querySelectorAll(".bel_map g");
                        showSettlementInfo(data[item.id], newHoverElement, data[item.id].cities);
                        clickCount = 0;
                    }
                } else {
                    checky = item
                    clickCount = 1;
                }

            });
        }
    }
    back_button.addEventListener("click",  function() {
        document.querySelector(`.bel_map`).classList.remove("bel_map");
        rb.classList.add("bel_map");
        showSettlementInfo(country, hoverElement, obls)
    })

    showSettlementInfo(country, hoverElement, obls)
}



fetchData();