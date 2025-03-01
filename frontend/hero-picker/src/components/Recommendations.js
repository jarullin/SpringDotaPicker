import React from 'react';

function Recommendations({ recommendations }) {
  return (
    <div>
      <h2>Recommended Heroes</h2>
      {Object.entries(recommendations).map(([role, heroes]) => (
        <div key={role}>
          <h3>{role}</h3>
          <ul>
            {heroes.map(hero => (
              <li key={hero.id}>
                {hero.name} (Winrate: {(hero.winRate * 100).toFixed(2)}%)
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}

export default Recommendations;