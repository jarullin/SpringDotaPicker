import React from 'react';
import '../App.css';

function Recommendations({ recommendations }) {
  return (
    <div>
      <h2>Recommended Heroes</h2>
      {Object.entries(recommendations).map(([k, v]) => {
        return (
          <div key={k}>
            <h3>{k.toUpperCase()}</h3>
            <div style={{display:'flex', justifyContent:'center'}}>
              {v.map((hero) => {
                console.log(hero.imageLink);
                return (
                  <div key={hero.hero.id} >
                    <div style={{ position: 'relative', display: 'inline-block' }}>
                      <b style={{position:'absolute', right:5, bottom:5, zIndex:2, fontFamily:'a_LCDNova', color:'white', fontSize:21
                        ,textShadow: '2px 2px 0 black, -2px -2px 0 black, -2px 2px 0 black, 2px -2px 0 black'

                      }} >
                        {(hero.winProb).toFixed(2)}
                      </b>
                      <img style={{ display:'block',
                        height: 72,
                        width: 128
                      }}
                        src={"https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/" + hero.hero.imageLink + ".png"}>
                      </img>
                    </div>
                    {/* {hero.hero.name} (Winscore: {(hero.winProb).toFixed(2)}) */}
                  </div>);
              })}
            </div>
          </div>)
      })}
    </div >
  );
}

export default Recommendations;