package com.example.blinkstory.elements;

import dagger.Subcomponent;

@ElementScope
@Subcomponent(modules = {ElementModule.class})
public interface ElementComponent {
    void inject(ElementsActivity elementsActivity);
}
