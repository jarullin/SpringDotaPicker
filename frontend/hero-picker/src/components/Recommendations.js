import React from 'react';

function Recommendations({ recommendations }) {
  return (
    <div>
      <h2>Recommended Heroes</h2>
      {Object.entries(recommendations).map(([k, v]) => {
        return(
        <div key={k}>
          <h3>{k}</h3>
          <ul>
            {v.map((hero) => {
              console.log(hero.imageLink);
              return (<li key={hero.hero.id}>
                <img style={{
                                height: 72,
                                width: 128}}
                src={"https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/" + hero.hero.imageLink + ".png"}>
                                  </img>                  
                {hero.hero.name} (Winscore: {(hero.winProb).toFixed(2)})
              </li>);
            })}
          </ul>
        </div>)
      })}
    </div >
  );
}

export default Recommendations;