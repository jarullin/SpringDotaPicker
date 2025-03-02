import React, { useState } from 'react';


function HeroPicker({ heroes, selectedEnemies, onEnemySelect, onEnemyDeselect }) {
    const [disabledButtons, setDisabledButtons] = useState({});
    console.log(heroes);
    return (
        <div>
            <h2>Select Enemy Heroes (max 5)</h2>
            <div style={{ display: 'flex'}}>
                {selectedEnemies.map(enemy => (
                    <div key={enemy.id} style={{width:'20%'}}>
                        <input
                            type='image'
                            style={{
                                position:'relative',
                                width: '100%',
                                maxHeight: 126,
                                maxWidth: 225
                            }}
                            src={"https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/" + enemy.imageLink + ".png"}
                            onClick={() => {

                                setDisabledButtons((prev) => ({ ...prev, [enemy.id]: false }))
                                onEnemyDeselect(enemy.id)
                            }
                            }
                            alt={enemy.name}
                        />
                    </div>
                ))}
            </div>
            <div style={{ overflowY: 'auto', display: 'flex', flexWrap: 'wrap' }}>
                {heroes.map(hero => (
                    <div key={hero.id} style={{ margin: '5px' }}>
                        <input
                            type='image'
                            style={{
                                height: 72,
                                width: 128,
                                objectFit: "cover",
                                filter: disabledButtons[hero.id] ? "grayscale(100%)" : "none",
                                cursor: disabledButtons[hero.id] ? "not-allowed" : "pointer"
                            }}
                            src={"https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/" + hero.imageLink + ".png"}
                            onClick={() => {
                                if (selectedEnemies.length < 5) {
                                    setDisabledButtons((prev) => ({ ...prev, [hero.id]: true }))
                                    onEnemySelect(hero.id)
                                }
                            }}
                            disabled={disabledButtons[hero.id]}
                            alt={hero.name}
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}

export default HeroPicker;