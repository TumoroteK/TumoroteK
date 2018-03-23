package fr.aphp.tumorotek.decorator.factory;

import java.util.List;

public interface TKDecoratorFactory
{

   List<? extends Object> decorateListe(List<? extends Object> objets);
}
