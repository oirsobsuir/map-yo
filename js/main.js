let jsonData;

async function fetchData() {
    try {
        const response = await fetch('./trasnform/data/data.json');
        jsonData = await response.json();
    } catch (error) {
        console.error('Ошибка загрузки данных JSON:', error);
    }

    const{data: country, obls} = jsonData;

    let current = country;


    let hoverElement = document.querySelectorAll("g");
    let title = document.getElementById("title");
    let box = document.getElementsByClassName('box')[0];
    let values = document.getElementsByClassName('value');
    let numbers = document.getElementsByClassName('number');

    for(let i = 0; i < numbers.length; i++){
        numbers[i].textContent = current['n'+ (i + 1)]
    }


    for (let item of hoverElement) {
        item.addEventListener("mouseover", function() {
            console.log(obls[item.id])
            box.style.display = 'block'
            item.id === '6' ? title.textContent = 'город ' + obls[item.id].name : title.textContent = obls[item.id].name + ' область'
            for(let i = 0; i < values.length; i++){
                values[i].textContent = obls[item.id]['n'+ (i + 1)]
            }
        });

        item.addEventListener("mouseout", function() {
            box.style.display = 'none'
        });
    }

}

fetchData();