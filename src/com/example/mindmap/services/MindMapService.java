package com.example.mindmap.services;

import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.MapElement;
import com.example.mindmap.entities.TextNode;
import com.example.mindmap.entities.User;
import com.example.mindmap.repositories.JdbcMapElementRepository;
import com.example.mindmap.repositories.JdbcMindMapRepository;

import java.util.List;

public class MindMapService {

    private final JdbcMindMapRepository mapRepository;
    private final JdbcMapElementRepository elementRepository;

    public MindMapService(JdbcMindMapRepository mapRepository,
                          JdbcMapElementRepository elementRepository) {
        this.mapRepository = mapRepository;
        this.elementRepository = elementRepository;
    }

    // --- Робота з мапами ---

    public MindMap createMap(String title, User owner) {
        MindMap map = new MindMap();
        map.setTitle(title);
        map.setOwner(owner);
        mapRepository.save(map);
        return map;
    }

    /**public List<MindMap> getMapsByUser(User user) {
        return mapRepository.getAllByUser(user.getId());
    } */
    public List<MindMap> getMapsByUser(User user) {
        return mapRepository.getAllByUser(user);
    }

    // --- Робота з елементами мапи ---

    /**
     * Завантажити всі елементи (вузли) для конкретної мапи.
     */
    public List<MapElement> getElementsForMap(MindMap map) {
        return elementRepository.findByMapId(map.getId());
    }

    /**
     * Додати новий текстовий вузол до мапи (створюється і одразу зберігається в БД).
     */
    public MapElement addTextNode(MindMap map, float x, float y, String text) {
        TextNode node = new TextNode(x, y, text);
        elementRepository.save(node, map.getId());
        return node;
    }

    /**
     * Оновити елемент мапи (координати, текст, інше) в БД.
     */
    public void updateElement(MapElement element) {
        elementRepository.update(element);
    }

    /**
     * Видалити елемент мапи з БД.
     */
    public void deleteElement(MapElement element) {
        if (element.getId() != 0) {
            elementRepository.delete(element.getId());
        }
    }
}
